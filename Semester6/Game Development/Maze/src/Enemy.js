export default class Enemy {
  constructor(scene, maze, startX, startZ) {
    this.scene = scene
    this.maze = maze
    this.size = 0.4
    this.speed = 3  // adjust speed
    this.targetCell = null

    this.mesh = scene.physics.add.sphere(
      { x: startX, y: 1, z: startZ, radius: this.size, mass: 1 },
      { lambert: { color: 0x9900ff } }
    )
    this.mesh.body.setFriction(1)
    this.setNewDirection()
  }

  setNewDirection() {
    // Find possible directions (not walls)
    const possible = []
    const { x, z } = this.mesh.position
    const cSize = this.maze.cellSize
    const row = Math.round(z / cSize)
    const col = Math.round(x / cSize)
    const dirs = [
      { dx: 1, dz: 0 }, { dx: -1, dz: 0 },
      { dx: 0, dz: 1 }, { dx: 0, dz: -1 }
    ]
    for (const dir of dirs) {
      const nr = row + dir.dz
      const nc = col + dir.dx
      if (this.maze.isWalkable(nr, nc)) possible.push(dir)
    }
    // Pick one at random
    this.dir = possible[Math.floor(Math.random() * possible.length)] || {dx:0,dz:0}
    this.nextChange = Date.now() + 700 + Math.random()*600
  }

  update() {
    // Change direction every so often
    if (Date.now() > this.nextChange) {
      this.setNewDirection()
    }
    // Move in current direction
    const vx = this.dir.dx * this.speed
    const vz = this.dir.dz * this.speed
    this.mesh.body.setVelocity(vx, 0, vz)
  }
}