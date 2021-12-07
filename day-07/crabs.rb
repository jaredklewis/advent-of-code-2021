# frozen_string_literal: true

# https://adventofcode.com/2021/day/7

def parse(text)
  text.split(",").map(&:to_i)
end

def calc_optimal_fuel_cost(positions)
  pos_to_count = {}

  sorted_positions = positions.sort
  min = sorted_positions[0]
  max = sorted_positions[-1]

  sorted_positions.each do |pos|
    pos_to_count[pos] = (pos_to_count[pos] || 0) + 1
  end

  min_cost = nil
  (min..max).each do |target_pos|
    cost = 0
    pos_to_count.each do |pos, count|
      moves = (pos - target_pos).abs
      move_cost = if block_given?
                    yield moves
                  else
                    moves
                  end
      cost += move_cost * count
    end

    min_cost = cost if !min_cost || cost < min_cost
  end

  min_cost
end

def calc_triangular_optimal_fuel_cost(positions)
  calc_optimal_fuel_cost(positions) do |n|
    n * (n + 1) / 2
  end
end

# Data
example_positions = parse("16,1,2,0,4,2,7,1,2,14")
positions = parse(File.read("input.txt"))

# Part one example
example_cost = calc_optimal_fuel_cost(example_positions)
raise "Part one example incorrect, got #{example_cost}" unless example_cost == 37

# Part one
cost = calc_optimal_fuel_cost(positions)
puts "Part one - optimal fuel cost: #{cost}"

# Part two example
triangular_example_cost = calc_triangular_optimal_fuel_cost(example_positions)
unless triangular_example_cost == 168
  raise "Part two example incorrect, got #{triangular_example_cost}"
end

triangular_cost = calc_triangular_optimal_fuel_cost(positions)
puts "Part two - optimal triangular fuel cost: #{triangular_cost}"
