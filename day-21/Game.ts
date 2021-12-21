import fs from "fs";

export type Player = "p1" | "p2";

export type Winner = Player | false;

export type PlayerState = Readonly<{
  position: number;
  score: number;
}>;

export type GameState = {
  turn: Player;
  p1: PlayerState;
  p2: PlayerState;
  winner: Winner;
};

export function parseGameStart(file: string): GameState {
  const input = fs.readFileSync(file, { encoding: "utf-8" });
  const [p1, p2] = input.split("\n");
  if (!p1 || !p2) {
    throw new Error("Error parsing players");
  }

  const findStart = (raw: string): number => {
    return parseInt(raw.match(/: ([0-9]+)$/)?.[1]!!);
  };

  return {
    p1: { position: findStart(p1), score: 0 },
    p2: { position: findStart(p2), score: 0 },
    winner: false,
    turn: "p1",
  };
}

function move(startPosition: number, moves: number): number {
  const total = (startPosition + moves) % 10;
  return total === 0 ? 10 : total;
}

function advancePlayer(player: PlayerState, roll: number): PlayerState {
  const position = move(player.position, roll);
  const score = player.score + position;

  return { position, score };
}

export function playTurn(
  state: GameState,
  roll: number,
  winningScore: number
): GameState {
  const playerState = advancePlayer(state[state.turn], roll);
  let winner = playerState.score >= winningScore ? state.turn : false;

  return {
    ...state,
    [state.turn]: playerState,
    winner,
    turn: state.turn === "p1" ? "p2" : "p1",
  };
}

export function otherPlayer(player: Player): Player {
  return player === "p1" ? "p2" : "p1";
}
