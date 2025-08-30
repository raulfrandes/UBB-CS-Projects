export default class GameScene extends Phaser.Scene {
    constructor() {
        super({ key: 'GameScene' });
    }

    preload() {
        this.load.spritesheet('tiles', 'assets/environment_set.png',{
            frameWidth: 17,
            frameHeight: 16
        });
        this.load.image('background', 'assets/background.png');
        this.load.spritesheet('player1', 'assets/player_set.png', {
            frameWidth: 16,
            frameHeight: 17.5
        });
        this.load.spritesheet('player2', 'assets/player_set.png', {
            frameWidth: 16,
            frameHeight: 16
        });
        this.load.spritesheet('bullet', 'assets/bullet_set.png', {
            frameWidth: 21,
            frameHeight: 16
        });
        this.load.spritesheet('weapons', 'assets/weapons_set.png', {
            frameWidth: 25,
            frameHeight: 16
        });
        this.load.audio('shoot', 'assets/shoot.mp3');
        this.load.audio('hit', 'assets/hit.mp3');
    }

    create() {
        this.add.image(400, 300, 'background').setDepth(-1).setScale(2);

        // Add platforms
        const groundTile = this.add.tileSprite(400, 550, 800, 32, 'tiles', 9);
        groundTile.setTileScale(2, 2);
        this.physics.add.existing(groundTile, true);

        this.platforms = this.physics.add.staticGroup();
        for (let i = 0; i < 3; i++) {
            this.platforms.create(152 + i * 32, 450, 'tiles', 9).setScale(2).refreshBody();
            this.platforms.create(152 + i * 32, 250, 'tiles', 9).setScale(2).refreshBody();
            this.platforms.create(582 + i * 32, 450, 'tiles', 9).setScale(2).refreshBody();
            this.platforms.create(582 + i * 32, 250, 'tiles', 9).setScale(2).refreshBody();
        }
        for (let i = 0; i < 6; i++) {
            this.platforms.create(315 + i * 32, 350, 'tiles', 9).setScale(2).refreshBody();
        }
        


        // Player 1
        this.player1 = this.physics.add.sprite(100, 450, 'player1').setScale(3);
        this.player1.setBounce(0.2);
        this.player1.setCollideWorldBounds(true);
        this.weapon1 = this.add.sprite(0, 0, 'weapons', 2).setScale(1.5);

        // Player 2
        this.player2 = this.physics.add.sprite(700, 450, 'player2').setScale(3);
        this.player2.setBounce(0.2);
        this.player2.setCollideWorldBounds(true);
        this.player2.flipX = true;
        this.weapon2 = this.add.sprite(0, 0, 'weapons', 3).setScale(1.5);

        // Collisions
        this.physics.add.collider(this.player1, groundTile);
        this.physics.add.collider(this.player2, groundTile);
        this.physics.add.collider(this.player1, this.platforms);
        this.physics.add.collider(this.player2, this.platforms);

        // Player input
        this.cursors = this.input.keyboard.createCursorKeys();
        this.WASD = this.input.keyboard.addKeys({
            up: Phaser.Input.Keyboard.KeyCodes.W,
            left: Phaser.Input.Keyboard.KeyCodes.A,
            right: Phaser.Input.Keyboard.KeyCodes.D
        });

        // Animations (simple flipping instead of animating)
        this.player1Direction = 'right';
        this.player2Direction = 'left';

        // Score tracking
        this.player1Score = 0;
        this.player2Score = 0;

        // Scoreboard UI
        this.scoreText = this.add.text(10, 20, 'Score: P1 - 0  |  P2 - 0', {
            fontSize: '20px',
            fill: '#ffffff'
        });

        // Projectile groups
        this.player1Projectiles = this.physics.add.group();
        this.player2Projectiles = this.physics.add.group();

        // Health System
        this.player1Health = 3;
        this.player2Health = 3;
        this.healthText = this.add.text(475, 20, `Health: P1 - 3  |  P2 - 3`, {
            fontSize: '20px',
            fill: '#ffffff'
        });

        // Collider: P1 projectile hits P2
        this.physics.add.overlap(this.player1Projectiles, this.player2, (player, projectile) => {
            if (!projectile.active) return;
        
            this.hitSound.play();
            projectile.disableBody(true, true);
            this.player2.setVelocityX(projectile.x < this.player2.x ? 200 : -200);
            this.player2.setVelocityY(-100);
            this.player2Health--;
        
            if (this.player2Health <= 0) {
                this.player1Score++;
                this.resetRound();
            } else {
                this.updateHealthUI();
            }
        }, null, this);
        

        // Collider: P2 projectile hits P1
        this.physics.add.overlap(this.player2Projectiles, this.player1, (player, projectile) => {
            if (!projectile.active) return;
        
            this.hitSound.play();
            projectile.disableBody(true, true);
            this.player1.setVelocityX(projectile.x < this.player1.x ? 200 : -200);
            this.player1.setVelocityY(-100);
            this.player1Health--;
        
            if (this.player1Health <= 0) {
                this.player2Score++;
                this.resetRound();
            } else {
                this.updateHealthUI();
            }
        }, null, this);
        

        // Fire buttons
        this.fireKeys = this.input.keyboard.addKeys({
            player1Fire: Phaser.Input.Keyboard.KeyCodes.SPACE,
            player2Fire: Phaser.Input.Keyboard.KeyCodes.SHIFT
        });

        // Shooting cooldown
        this.lastFired = {
            player1: 0,
            player2: 0
        };
        this.fireCooldown = 500; // milliseconds

        // Simple Animations
        this.anims.create({
            key: 'run',
            frames: this.anims.generateFrameNumbers('player1', { start: 6, end: 9 }),
            frameRate: 10,
            repeat: -1
        });
        this.anims.create({
            key: 'idle',
            frames: [{ key: 'player1', frame: 0 }],
            frameRate: 20
        });
        this.anims.create({
            key: 'jump',
            frames: [{ key: 'player1', frame: 1 }],
            frameRate: 10,
        });

        // Sound effects
        this.shootSound = this.sound.add('shoot');
        this.hitSound = this.sound.add('hit');

        // Muzzle flash particles
        this.muzzleSystem = this.add.particles(0, 0, 'bullet', {
            frame: 0,
            speed: { min: 100, max: 200 },
            scale: { start: 0.5, end: 0 },
            lifespan: 200,
            emitting: false
        });
    }

    update() {
        if (!this.player1?.body || !this.player2?.body) return;

        // Player 1 Constrols - Arrow Keys
        if (this.cursors.left.isDown) {
            this.player1.setVelocityX(-160);
            this.player1.setFlipX(true);
            this.player1.anims.play('run', true);
        } else if (this.cursors.right.isDown) {
            this.player1.setVelocityX(160);
            this.player1.setFlipX(false);
            this.player1.anims.play('run', true);
        } else {
            this.player1.setVelocityX(0);
            this.player1.anims.play('idle');
        }

        if (this.cursors.up.isDown && this.player1.body.touching.down) {
            this.player1.setVelocityY(-330);
            this.player1.anims.play('jump');
        }

        // Player 1 weapon position
        this.weapon1.x = this.player1.x + (this.player1.flipX ? -12 : 12);
        this.weapon1.y = this.player1.y + 5;
        this.weapon1.setFlipX(this.player1.flipX);

        // Player 2 Controls - WASD
        if (this.WASD.left.isDown) {
            this.player2.setVelocityX(-160);
            this.player2.setFlipX(true);
            this.player2.anims.play('run', true);
        } else if (this.WASD.right.isDown) {
            this.player2.setVelocityX(160);
            this.player2.setFlipX(false);
            this.player2.anims.play('run', true);
        } else {
            this.player2.setVelocityX(0);
            this.player2.anims.play('idle');
        }

        // Player 2 weapon position
        this.weapon2.x = this.player2.x + (this.player2.flipX ? -12 : 12);
        this.weapon2.y = this.player2.y + 5;
        this.weapon2.setFlipX(this.player2.flipX);
        
        if (this.WASD.up.isDown && this.player2.body.touching.down) {
            this.player2.setVelocityY(-330);
            this.player2.anims.play('jump');
        }

        const timeNow = this.time.now;

        // Player 1 fires (SPACE)
        if (Phaser.Input.Keyboard.JustDown(this.fireKeys.player1Fire) && timeNow - this.lastFired.player1 > this.fireCooldown) {
            const dir = this.player1.flipX ? 'left' : 'right';
            this.fireProjectile(this.player1, dir, this.player1Projectiles);
            this.lastFired.player1 = timeNow;
        }

        // Player 2 fires (SHIFT)
        if (Phaser.Input.Keyboard.JustDown(this.fireKeys.player2Fire) && timeNow - this.lastFired.player2 > this.fireCooldown) {
            const dir = this.player2.flipX ? 'left' : 'right';
            this.fireProjectile(this.player2, dir, this.player2Projectiles);
            this.lastFired.player2 = timeNow;
        }
    }

    fireProjectile(player, direction, group) {
        const bullet = group.create(player.x + (direction === 'left' ? -35 : 35), player.y,'bullet', (group === this.player1Projectiles ? 3 : 2))
            .setDisplaySize(10, 10)

        bullet.body.allowGravity = false;
        bullet.setVelocityX(direction === 'left' ? -300 : 300);
        bullet.setFlipX(player.flipX);

        this.shootSound.play();
        this.muzzleSystem.emitParticleAt(player.x + (direction === 'left' ? -35 : 35), player.y);
    }

    resetRound() {
        this.player1.setPosition(100, 450);
        this.player2.setPosition(700, 450);
        this.player1Projectiles.clear(true, true);
        this.player2Projectiles.clear(true, true);

        this.player1Health = 3;
        this.player2Health = 3;
        this.updateHealthUI();

        this.scoreText.setText(`P1: ${this.player1Score}  |  P2: ${this.player2Score}`);

        // Check win
        if (this.player1Score >= 2 || this.player2Score >= 2) {
            const winner = this.player1Score >= 2 ? 'Player 1' : 'Player 2';
            this.scene.start('MenuScene', { winner });
        }
    }

    updateHealthUI() {
        this.healthText.setText(`HP1: ${this.player1Health}  |  HP2: ${this.player2Health}`);
    }
}