const assert = require('assert');
const fs = require('fs');

// https://adventofcode.com/2021/day/5

const EXAMPLE = parse(`
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
`);

type Point = Readonly<{x: number, y: number}>;

type Line = Readonly<{start: Point; end: Point }>;

type Grid = Cell[][];

type Cell = { count: number };

type Input = {
    lines: readonly Line[];
    maxX: number;
    maxY: number;
}

function parse(text: string): Input {
    let maxY = 0;
    let maxX = 0;
    const lines = text.split("\n").filter((line) => !!line).map((line) => {
        const [start, end] = line.split(" -> ").map((str) => {
            const [x, y] = str.split(",").map((part) => {
                return parseInt(part);
            });

            if (x === undefined || y === undefined) {
                throw new Error("Unexpected input parsing coordinates");
            }

            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            return {x, y};
        });

        if (start === undefined || end === undefined) {
            throw new Error("Unexpected input parsing lines");
        }

        return {start, end};
    });

    return {lines, maxX, maxY};
}

function getCell(grid: Grid, y: number, x: number): Cell {
    const cell = grid[y]?.[x];
    if (!cell) {
        throw new Error(`Missing cell (x: ${x}, y: ${y})`);
    }
    return cell;
}

function buildGrid({lines, maxX, maxY}: Input, diagonals = false): Grid {
    const grid: Array<Array<{ count: number }>> = [];
    for (let y = 0; y <= maxY; y++) {
        const cols = [];
        for (let x = 0; x <= maxX; x++) {
            cols.push({ count: 0 });
        }
        grid.push(cols);
    }

    lines.forEach(({start, end}) => {
        if (start.x === end.x) {
            for (let y = Math.min(start.y, end.y); y <= Math.max(start.y, end.y); y++) {
                getCell(grid, y, start.x).count += 1;
            }
        } else if (start.y === end.y) {
            for (let x = Math.min(start.x, end.x); x <= Math.max(start.x, end.x); x++) {
                getCell(grid, start.y, x).count += 1;
            }
        } else if (diagonals) {
            let { x, y } = { ...start };

            while (true) {
                getCell(grid, y, x).count += 1;
                if (y < end.y) {
                    y++;
                }
                if (y > end.y) {
                    y--;
                }
                if (x < end.x) {
                    x++;
                } else if (x > end.x) {
                    x--;
                } else {
                    break;
                }

            }
        }
    });

    return grid;
}

function countDangerPoints(grid: Grid): number {
    let count = 0;
    for (const row of grid) {
        for (const cell of row) {
            if (cell.count > 1) {
                count++
            }
        }
    }

    return count;
}

// Parse lines input
const lines = parse(fs.readFileSync("input.txt", { encoding: "utf-8" }));

// Part one
assert.equal(countDangerPoints(buildGrid(EXAMPLE)), 5);
const grid = buildGrid(lines);
console.log("Part one - count of overlapping points: ", countDangerPoints(grid));

// Part two
assert.equal(countDangerPoints(buildGrid(EXAMPLE, true)), 12);
const gridWithDiagonals = buildGrid(lines, true);
console.log("Part two - count of overlapping points: ", countDangerPoints(gridWithDiagonals));
