from __future__ import annotations

from risk_graph import calc_lowest_risk
from risk_map import parse_risk_map, enlarge_risk_map

# Data
example = parse_risk_map("example.txt")
cave_map = parse_risk_map("input.txt")

# Part one

assert calc_lowest_risk(example) == 40
print("Part one - least risk: ", calc_lowest_risk(cave_map))


# Part two
enlarged_example = enlarge_risk_map(example)
expected_enlarged_example = parse_risk_map("expected-enlarged-example.txt")
assert enlarged_example == expected_enlarged_example

full_map = enlarge_risk_map(cave_map)
print("Part two - least risk: ", calc_lowest_risk(full_map))

