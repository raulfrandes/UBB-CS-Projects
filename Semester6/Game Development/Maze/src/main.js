import MazeScene from './MazeScene.js';

const { PhysicsLoader, Project } = ENABLE3D;

PhysicsLoader('/lib/ammo/kripken', () => {
  new Project({ scenes: [MazeScene], antialias: true });
});