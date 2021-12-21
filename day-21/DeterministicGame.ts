import { GameState, otherPlayer, playTurn } from "./Game";

export type DeterministicGameResult = Readonly<{
  losingScore: number;
  rolls: number;
}>;

export function playDeterministicGame(
  start: GameState
): DeterministicGameResult {
  let deterministicDie = 0;
  const tripleRollDeterministicDie = () => {
    let rollTotal = 0;
    for (let i = 0; i < 3; i++) {
      deterministicDie += 1;
      if (deterministicDie > 100) {
        deterministicDie = 1;
      }
      rollTotal += deterministicDie;
    }

    return rollTotal;
  };

  let rolls = 0;
  let state = start;

  while (!state.winner) {
    state = playTurn(state, tripleRollDeterministicDie(), 1000);
    rolls += 3;
  }

  const loser = state[otherPlayer(state.winner)];

  return {
    rolls,
    losingScore: loser.score,
  };
}
