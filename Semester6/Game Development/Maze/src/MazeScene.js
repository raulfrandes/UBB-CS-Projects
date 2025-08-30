import Player from './Player.js';
import { buildMaze, MAZE_LAYOUT, getGoalPosition, getFreeCells, cellSize, isWalkable } from './Maze.js';
import Enemy from './Enemy.js';
const { Scene3D } = ENABLE3D;

export default class MazeScene extends Scene3D {
  constructor() {
    super('MazeScene');
  }

  create() {
    this.warpSpeed('light', 'grid', 'sky', 'orbitControls');
    // this.physics.debug.enable();

    this.camera.position.set(8, 14, 14);
    this.camera.lookAt(0, 0, 0);

    // Build the maze and floor
    const rows = MAZE_LAYOUT.length;
    const cols = MAZE_LAYOUT[0].length;
    const width = cols * cellSize;
    const depth = rows * cellSize;
    const xOffset = -width / 2 + cellSize / 2;
    const zOffset = -depth / 2 + cellSize / 2;
    const midX = (cols * cellSize) / 2 - cellSize;
    const midZ = (rows * cellSize) / 2 - cellSize;
    buildMaze(this, cellSize);

    // Camera above, angled down
    this.camera.position.set(midX, 28, midZ + 2);
    this.camera.lookAt(midX, 0, midZ);

    // Find player start position (first free cell)
    let startX = 1, startZ = 1;
    for (let z = 0; z < MAZE_LAYOUT.length; z++) {
        for (let x = 0; x < MAZE_LAYOUT[0].length; x++) {
            if (MAZE_LAYOUT[z][x] === 0) {
            startX = x;
            startZ = z;
            break;
            }
        }
        if (startX !== 1 || startZ !== 1) break;
    }

    this.player = new Player(
        this,
        {
            x: startX * cellSize + xOffset,
            y: 2,
            z: startZ * cellSize + zOffset
        }
    );

    this.enemies = [];

    // Example: spawn 3 enemies at random walkable locations (not at player/goal)
    const enemyStarts = [
        {x: 3, z: 3},
        {x: 5, z: 1},
        {x: 1, z: 5}
    ];
    // Convert to world coordinates:
    for (const pos of enemyStarts) {
        this.enemies.push(
            new Enemy(
            this,
            { cellSize, isWalkable },
            pos.x * cellSize,
            pos.z * cellSize
            )
        );
    }

    // Add visual-only goal (no physics body)
    const goalPos = getGoalPosition(cellSize);
    const goalGeom = new ENABLE3D.THREE.SphereGeometry(0.4, 20, 20);
    const goalMat = new ENABLE3D.THREE.MeshLambertMaterial({ color: 0x00ff66 });
    this.goalMesh = new ENABLE3D.THREE.Mesh(goalGeom, goalMat);
    this.goalMesh.position.set(goalPos.x, 1, goalPos.z);
    this.scene.add(this.goalMesh);
    this.goalPos = goalPos; // save for win detection

    // Win flag
    this.hasWon = false;

    // --- Place coins (not at start or goal) ---
    const allFreeCells = getFreeCells(cellSize);

    // Exclude start and goal cells
    const startCell = { x: startX, z: startZ };
    const goalCell = allFreeCells.find(c => c.x === goalPos.x && c.z === goalPos.z)?.cell || { x: -1, z: -1 };

    // Place a coin at every other open cell except start and goal
    this.coins = [];
    allFreeCells.forEach(pos => {
        if (
            (pos.cell.x !== startCell.x || pos.cell.z !== startCell.z) &&
            (pos.cell.x !== goalCell.x || pos.cell.z !== goalCell.z)
        ) {
            // Visual-only mesh, no physics
            const coinGeom = new ENABLE3D.THREE.SphereGeometry(0.25, 16, 16);
            const coinMat = new ENABLE3D.THREE.MeshLambertMaterial({ color: 0xffa500 });
            const mesh = new ENABLE3D.THREE.Mesh(coinGeom, coinMat);
            mesh.position.set(pos.x, 1, pos.z);
            mesh.isCoin = true;
            this.scene.add(mesh);
            this.coins.push(mesh);
        }
    });

    // --- Add UI for coins ---
    this.coinCount = 0;
    this.coinTotal = this.coins.length;
    if (!document.getElementById('ui-coins')) {
        const ui = document.createElement('div');
        ui.id = 'ui-coins';
        ui.style.position = 'absolute';
        ui.style.top = '10px';
        ui.style.right = '16px';
        ui.style.fontSize = '24px';
        ui.style.color = 'orange';
        ui.style.fontFamily = 'Arial, sans-serif';
        ui.style.zIndex = '1000';
        document.body.appendChild(ui);
    }
    this.updateCoinUI();

    // Timer
    this.startTime = performance.now();
    this.elapsed = 0;
    if (!document.getElementById('ui-timer')) {
        const timerUI = document.createElement('div');
        timerUI.id = 'ui-timer';
        timerUI.style.position = 'absolute';
        timerUI.style.top = '10px';
        timerUI.style.left = '16px';
        timerUI.style.fontSize = '24px';
        timerUI.style.color = '#00d4d4';
        timerUI.style.fontFamily = 'Arial, sans-serif';
        timerUI.style.zIndex = '1000';
        document.body.appendChild(timerUI);
    }
    this.updateTimerUI();

    this.keys = { up: false, down: false, left: false, right: false };

    window.addEventListener('keydown', e => this.handleKey(e, true));
    window.addEventListener('keyup', e => this.handleKey(e, false));
  }

  updateCoinUI() {
    const ui = document.getElementById('ui-coins');
    if (ui) ui.innerText = `Coins: ${this.coinCount} / ${this.coinTotal}`;
  }

  updateTimerUI() {
    const ui = document.getElementById('ui-timer');
    if (ui) ui.innerText = `Time: ${this.elapsed.toFixed(1)} s`;
  }


  handleKey(e, isDown) {
    switch (e.code) {
      case 'ArrowUp':
      case 'KeyW': this.keys.up = isDown; break;
      case 'ArrowDown':
      case 'KeyS': this.keys.down = isDown; break;
      case 'ArrowLeft':
      case 'KeyA': this.keys.left = isDown; break;
      case 'ArrowRight':
      case 'KeyD': this.keys.right = isDown; break;
    }
  }

  update() {
    if (!this.hasWon) {
        this.elapsed = (performance.now() - this.startTime) / 1000;
        this.updateTimerUI();
    }

    // Follow the player
    if (this.player) {
        // Camera "follows" from above and behind the ball
        const p = this.player.position;
        this.camera.position.lerp(
            { x: p.x, y: p.y + 11, z: p.z + 7 },
            0.09
        );
        this.camera.lookAt(p.x, p.y, p.z);
    }

    if (this.player) {
        const ballScreen = worldToScreen(this.player.position, this.camera, this.renderer);
        const vignette = document.getElementById('vignette');
        vignette.style.background = `
            radial-gradient(
                circle at ${ballScreen.x}px ${ballScreen.y}px,
                rgba(0,0,0,0) 120px,
                rgba(0,0,0,0.92) 480px
            )
        `;
    }

    if (!this.player || !this.player.body || this.hasWon) return;

    for (const enemy of this.enemies) {
        enemy.update();
        // Check collision with player:
        const dx = this.player.position.x - enemy.mesh.position.x;
        const dy = this.player.position.y - enemy.mesh.position.y
        const dz = this.player.position.z - enemy.mesh.position.z;
        const dist2 = dx * dx + dy * dy + dz * dz
        if (dist2 < 1.0) {
            this.hasLost = true;
            this.showGameOverScreen();
            break;
        }
    }

    let forceX = 0, forceZ = 0;
    if (this.keys.up) forceZ -= 2;
    if (this.keys.down) forceZ += 2;
    if (this.keys.left) forceX -= 2;
    if (this.keys.right) forceX += 2;

    this.player.move({ forceX, forceZ });

    if (this.coins && this.coins.length) {
        for (let i = this.coins.length - 1; i >= 0; i--) {
            const coin = this.coins[i];
            const dx = this.player.position.x - coin.position.x;
            const dy = this.player.position.y - coin.position.y;
            const dz = this.player.position.z - coin.position.z;
            if ((dx*dx + dy*dy + dz*dz) < 0.36) { // radius 0.25 + ball 0.4, give margin
            this.scene.remove(coin);
            this.coins.splice(i, 1);
            this.coinCount++;
            this.updateCoinUI();
            }
        }
    }

    const px = this.player.position.x, py = this.player.position.y, pz = this.player.position.z;
    const gx = this.goalPos.x, gy = 1, gz = this.goalPos.z;
    const dist2 = (px - gx) ** 2 + (py - gy) ** 2 + (pz - gz) ** 2;
    if (dist2 < 0.7 * 0.7 && this.coinCount === this.coinTotal) {
        this.hasWon = true;
        this.showWinScreen();
    }
  }

  showGameOverScreen() {
    let loseOverlay = document.getElementById('ui-lose');
    if (!loseOverlay) {
        loseOverlay = document.createElement('div');
        loseOverlay.id = 'ui-lose';
        loseOverlay.style.position = 'fixed';
        loseOverlay.style.top = '50%';
        loseOverlay.style.left = '50%';
        loseOverlay.style.transform = 'translate(-50%, -50%)';
        loseOverlay.style.background = 'rgba(20,0,0,0.89)';
        loseOverlay.style.padding = '36px 48px';
        loseOverlay.style.borderRadius = '22px';
        loseOverlay.style.textAlign = 'center';
        loseOverlay.style.color = '#ffcccc';
        loseOverlay.style.fontSize = '2rem';
        loseOverlay.style.zIndex = '1100';
        document.body.appendChild(loseOverlay);
    }
    loseOverlay.innerHTML = `
        <div>💀 <b>Game Over!</b> 💀</div>
        <div style="margin:18px 0 8px 0;">
            <b>Time:</b> ${this.elapsed.toFixed(2)} s<br>
            <b>Coins:</b> ${this.coinCount} / ${this.coinTotal}
        </div>
        <button onclick="location.reload()" style="margin-top:16px;font-size:1.2rem;padding:7px 26px;border-radius:8px;border:none;background:#bbb;color:#222;cursor:pointer;">Restart</button>
    `;
  }

  showWinScreen() {
    let winOverlay = document.getElementById('ui-win');
    if (!winOverlay) {
        winOverlay = document.createElement('div');
        winOverlay.id = 'ui-win';
        winOverlay.style.position = 'fixed';
        winOverlay.style.top = '50%';
        winOverlay.style.left = '50%';
        winOverlay.style.transform = 'translate(-50%, -50%)';
        winOverlay.style.background = 'rgba(0,0,0,0.88)';
        winOverlay.style.padding = '40px 50px';
        winOverlay.style.borderRadius = '24px';
        winOverlay.style.textAlign = 'center';
        winOverlay.style.color = 'white';
        winOverlay.style.fontSize = '2rem';
        winOverlay.style.zIndex = '1000';
        document.body.appendChild(winOverlay);
    }
    winOverlay.innerHTML = `
        <div>🎉 <b>Congratulations!</b> 🎉</div>
        <div style="margin:18px 0 8px 0;">
            <b>Time:</b> ${this.elapsed.toFixed(2)} s<br>
            <b>Coins:</b> ${this.coinCount} / ${this.coinTotal}
        </div>
        <button onclick="location.reload()" style="margin-top:16px;font-size:1.2rem;padding:7px 26px;border-radius:8px;border:none;background:#ffd700;color:#222;cursor:pointer;">Restart</button>
    `;
  }
}

function worldToScreen(pos, camera, renderer) {
  // Convert 3D world coordinates to screen coordinates
  const width = window.innerWidth;
  const height = window.innerHeight;
  const vector = pos.clone().project(camera);
  const x = (vector.x + 1) / 2 * width;
  const y = (-vector.y + 1) / 2 * height;
  return { x, y };
}