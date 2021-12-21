import { GameState, playTurn } from "./Game";

const TRIPLE_DIRAC_ROLLS = (function () {
  const map = new Map<number, { value: number; count: number }>();
  for (let i = 1; i < 4; i++) {
    for (let j = 1; j < 4; j++) {
      for (let k = 1; k < 4; k++) {
        const roll = i + j + k;
        const current = map.get(roll);
        if (current) {
          current.count += 1;
        } else {
          map.set(roll, { value: roll, count: 1 });
        }
      }
    }
  }
  return Array.from(map.values());
})();

export function playQuantumGame(start: GameState): bigint {
  const wins = { p1: 0n, p2: 0n };

  const spawnGame = (state: GameState, universes = 1n) => {
    TRIPLE_DIRAC_ROLLS.forEach((roll) => {
      const next = playTurn(state, roll.value, 21);
      const count = universes * BigInt(roll.count);
      if (next.winner) {
        wins[next.winner] += count;
      } else {
        spawnGame(next, count);
      }
    });
  };

  spawnGame(start);

  return wins.p1 > wins.p2 ? wins.p1 : wins.p2;
}
