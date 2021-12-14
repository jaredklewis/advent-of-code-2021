const assert = require("assert");
const fs = require("fs");

// https://adventofcode.com/2021/day/14

type Input = Readonly<{
  template: string;
  rules: Map<string, string>;
}>;

function parse(file: string): Input {
  const text = fs.readFileSync(file, { encoding: "utf-8" });

  const [template, pairs] = text.split("\n\n");

  if (!template || typeof template !== "string") {
    throw new Error("Unable to parse input.");
  }

  const rules = new Map<string, string>();
  pairs
    .split("\n")
    .filter((line: string) => !!line)
    .forEach((line: string) => {
      const [pair, insertion] = line.split(" -> ");

      if (!pair || !insertion) {
        throw new Error(`Unable to parse insertion rule: ${line}`);
      }

      rules.set(pair, insertion);
    });

  return {
    template,
    rules,
  };
}

function getLettersFromPair(pair: string): [string, string] {
  const [left, right] = pair.split("");
  if (!left || !right) {
    throw new Error(`Error parsing pair: ${pair}`);
  }

  return [left, right];
}

function calcFrequencySpread(
  { template, rules }: Input,
  steps: number
): number {
  let pairs = new Map<string, number>();

  // Setup pairs from the template
  for (let i = 0; i < template.length - 1; i++) {
    const left = template[i];
    const right = template[i + 1];
    if (!left || !right) {
      throw new Error("Error iterating pairs.");
    }
    const pair = left + right;
    pairs.set(pair, (pairs.get(pair) || 0) + 1);
  }

  // Run the steps
  Array.from({ length: steps }, () => {
    const nextPairs = new Map<string, number>();

    pairs.forEach((count, pair) => {
      const [left, right] = getLettersFromPair(pair);
      const insertion = rules.get(pair);
      if (!insertion) {
        throw new Error(`No insertion for pair ${pair}`);
      }
      const leftPair = left + insertion;
      const rightPair = insertion + right;

      nextPairs.set(leftPair, (nextPairs.get(leftPair) || 0) + count);
      nextPairs.set(rightPair, (nextPairs.get(rightPair) || 0) + count);
    });
    pairs = nextPairs;
  });

  // Count the letters
  const frequencies = new Map<string, number>();

  // Add the left character of the very first pair (see below)
  frequencies.set(template[0]!!, 1);

  pairs.forEach((count, pair) => {
    // We can throw away the left letter of every pair,
    // because it is the right of another pair.
    const [, right] = getLettersFromPair(pair);
    frequencies.set(right, (frequencies.get(right) || 0) + count);
  });

  // Find the spread
  let min = Number.POSITIVE_INFINITY;
  let max = 0;
  frequencies.forEach((value: number) => {
    min = Math.min(min, value);
    max = Math.max(max, value);
  });

  return max - min;
}

// Data
const example = parse("example.txt");
const input = parse("input.txt");

// Part one example
assert.equal(calcFrequencySpread(example, 10), 1588);

// Part one
console.log(
  "Part one - frequency spread after 10 steps: ",
  calcFrequencySpread(input, 10)
);

// Part two example
assert.equal(calcFrequencySpread(example, 40), 2188189693529);

// Part two example
console.log(
  "Part two - frequency spread after 40 steps: ",
  calcFrequencySpread(input, 40)
);
