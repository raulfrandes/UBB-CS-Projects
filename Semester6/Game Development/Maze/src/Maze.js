const { THREE } = ENABLE3D;

// Maze layout: 1 = wall, 0 = free
export const MAZE_LAYOUT = [
  [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
  [1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,1],
  [1,0,1,1,0,1,0,1,1,1,1,0,1,1,1,1,0,1],
  [1,0,1,0,0,0,0,0,0,0,1,0,0,0,0,1,0,1],
  [1,0,1,0,1,1,1,1,1,0,1,1,1,1,0,1,0,1],
  [1,0,0,0,0,0,1,0,1,0,0,0,0,1,0,0,0,1],
  [1,0,1,1,1,0,1,0,1,1,1,1,0,1,1,1,1,1],
  [1,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,1],
  [1,0,1,0,1,1,1,1,1,0,1,1,1,1,1,1,0,1],
  [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]
];

export const cellSize = 2;

export function buildMaze(scene, cellSize = 2) {
  const rows = MAZE_LAYOUT.length;
  const cols = MAZE_LAYOUT[0].length;
  const width = cols * cellSize;
  const depth = rows * cellSize;

  // Center offset so maze and ground are centered at 0,0
  const xOffset = -width / 2 + cellSize / 2;
  const zOffset = -depth / 2 + cellSize / 2;

  // Build floor, centered under the maze
  scene.physics.add.ground(
    { width, height: depth, y: 0, x: 0, z: 0 },
    { lambert: { color: 0x555555 } }
  );

  // Build walls, positioned using offset
  MAZE_LAYOUT.forEach((row, z) => {
    row.forEach((cell, x) => {
      if (cell === 1) {
        scene.physics.add.box(
          {
            x: x * cellSize + xOffset,
            y: 1,
            z: z * cellSize + zOffset,
            width: cellSize,
            height: 2,
            depth: cellSize,
            mass: 0
          },
          { lambert: { color: 0x222299 } }
        );
      }
    });
  });
}

export function getGoalPosition(cellSize = 2) {
  const rows = MAZE_LAYOUT.length;
  const cols = MAZE_LAYOUT[0].length;
  const width = cols * cellSize;
  const depth = rows * cellSize;
  const xOffset = -width / 2 + cellSize / 2;
  const zOffset = -depth / 2 + cellSize / 2;

  // Find last open cell (scans from bottom right)
  for (let z = rows - 1; z >= 0; z--) {
    for (let x = cols - 1; x >= 0; x--) {
      if (MAZE_LAYOUT[z][x] === 0) {
        return {
          x: x * cellSize + xOffset,
          y: 1,
          z: z * cellSize + zOffset
        };
      }
    }
  }
  // Fallback to (0,0)
  return { x: xOffset, y: 1, z: zOffset };
}

export function getFreeCells(cellSize = 2) {
  const rows = MAZE_LAYOUT.length;
  const cols = MAZE_LAYOUT[0].length;
  const width = cols * cellSize;
  const depth = rows * cellSize;
  const xOffset = -width / 2 + cellSize / 2;
  const zOffset = -depth / 2 + cellSize / 2;
  const cells = [];

  for (let z = 0; z < rows; z++) {
    for (let x = 0; x < cols; x++) {
      if (MAZE_LAYOUT[z][x] === 0) {
        cells.push({
          x: x * cellSize + xOffset,
          y: 1,
          z: z * cellSize + zOffset,
          cell: { x, z }
        });
      }
    }
  }
  return cells;
}

export function isWalkable(row, col) {
  // Protect against out-of-bounds
  if (
    row < 0 ||
    row >= MAZE_LAYOUT.length ||
    col < 0 ||
    col >= MAZE_LAYOUT[0].length
  ) return false;
  return MAZE_LAYOUT[row][col] === 0;
}