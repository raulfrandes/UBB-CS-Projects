const { THREE } = ENABLE3D;

export default class Player {
  constructor(scene, { x = 0, y = 2, z = 0 } = {}) {
    this.scene = scene;

    // Create the player sphere with physics
    this.mesh = scene.physics.add.sphere(
      { x, y, z, radius: 0.5, mass: 1, friction: 0.3 },
      { lambert: { color: 0x00ff99 } }
    );
  }

  // Movement methods
  move({ forceX = 0, forceZ = 0 }) {
    if (!this.mesh || !this.mesh.body) return;
    if (forceX !== 0) this.mesh.body.applyForceX(forceX);
    if (forceZ !== 0) this.mesh.body.applyForceZ(forceZ);
  }

  get position() {
    return this.mesh ? this.mesh.position : null;
  }

  get body() {
    return this.mesh ? this.mesh.body : null;
  }
}