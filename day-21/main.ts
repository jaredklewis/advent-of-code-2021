import assert from "assert";
import { playQuantumGame } from "./QuantumGame";
import { parseGameStart } from "./Game";
import { playDeterministicGame } from "./DeterministicGame";

// https://adventofcode.com/2021/day/21

// Data
const example = parseGameStart("example.txt");
const input = parseGameStart("input.txt");

// Part one example
const exampleResult = playDeterministicGame(example);
assert.equal(exampleResult.rolls * exampleResult.losingScore, 739_785);

// Part one
const result = playDeterministicGame(input);
console.log(
  "Part one - rolls times loser's score: ",
  result.rolls * result.losingScore
);

// Part two example
const quantumExample = playQuantumGame(example);
assert.equal(quantumExample, 444356092776315n);

// Part two
const quantumResult = playQuantumGame(input);
console.log("Part two - multiverse winner: ", quantumResult);
