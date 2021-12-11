# frozen_string_literal: true

# https://adventofcode.com/2021/day/11

class Octopus
  attr_reader :flashed, :neighbors

  def initialize(energy)
    @energy = energy
    @flashed = false
    @neighbors = []
  end

  def reset
    @flashed = false
  end

  def flash
    @flashed = true
    @energy = 0
    neighbors.each do |neighbor|
      neighbor&.step
    end
  end

  def step
    return if @flashed

    if @energy == 9
      flash
    else
      @energy += 1
    end
  end
end

def parse_grid(file)
  grid = File.read(file).split("\n").map do |line|
    line.chars.map do |energy|
      Octopus.new(energy.to_i)
    end
  end

  grid.each_with_index do |row, y|
    is_top = y.zero?
    is_bottom = y == (grid.size - 1)

    row.each_with_index do |octopus, x|
      is_left_edge = x.zero?
      is_right_edge = x == (row.size - 1)

      unless is_top
        octopus.neighbors << grid.dig(y - 1, x - 1) unless is_left_edge
        octopus.neighbors << grid.dig(y - 1, x)
        octopus.neighbors << grid.dig(y - 1, x + 1) unless is_right_edge
      end

      unless is_bottom
        octopus.neighbors << grid.dig(y + 1, x - 1) unless is_left_edge
        octopus.neighbors << grid.dig(y + 1, x)
        octopus.neighbors << grid.dig(y + 1, x + 1) unless is_right_edge
      end

      octopus.neighbors << grid.dig(y, x - 1) unless is_left_edge
      octopus.neighbors << grid.dig(y, x + 1) unless is_right_edge
    end
  end
end

def simulate(grid, steps)
  octopuses = grid.flatten

  flashes = 0
  steps.times do
    octopuses.each(&:reset)
             .each(&:step)
             .each do |o|
               flashes += 1 if o.flashed
             end
  end

  flashes
end

def find_boom(grid)
  octopuses = grid.flatten

  step = 0
  loop do
    step += 1
    count = octopuses.each(&:reset)
                     .each(&:step)
                     .map { |o| o.flashed ? 1 : 0 }
                     .sum
    break if count == 100
  end

  step
end

# Part one example
example_flashes = simulate(parse_grid("example.txt"), 100)
raise "Part one example incorrect, got #{example_flashes}" unless example_flashes == 1656

# Part one
flashes = simulate(parse_grid("input.txt"), 100)
puts "Part one - flashes: #{flashes}"

# Part two example
example_boom_step = find_boom(parse_grid("example.txt"))
raise "Part two example incorrect, got #{example_boom_step}" unless example_boom_step == 195

boom_step = find_boom(parse_grid("input.txt"))
puts "Part two - boom step: #{boom_step}"
