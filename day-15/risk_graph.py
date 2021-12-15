from __future__ import annotations
import heapq
from functools import total_ordering
from typing import List

from risk_map import RiskMap


@total_ordering
class Node:
    risk: int
    neighbors: List[Node]
    risk_from_start: int = float('inf')

    def __init__(self, risk: int):
        self.risk = risk
        self.neighbors = []

    def __eq__(self, other):
        return self.risk_from_start == other.risk_from_start

    def __lt__(self, other):
        return self.risk_from_start < other.risk_from_start


Grid = List[List[Node]]


def build_risk_graph(risk_map: RiskMap) -> Grid:
    cells = [list(map(lambda r: Node(risk=r), row)) for row in risk_map]

    row_count = len(cells)

    for row in range(row_count):
        col_count = len(cells[row])
        for col in range(col_count):
            has_up = (row - 1) >= 0
            has_down = (row + 1) < row_count
            has_left = (col - 1) >= 0
            has_right = (col + 1) < col_count

            cell = cells[row][col]
            cell.x = col
            cell.y = row
            if has_up:
                cell.neighbors.append(cells[row - 1][col])

            if has_down:
                cell.neighbors.append(cells[row + 1][col])

            if has_left:
                cell.neighbors.append(cells[row][col - 1])

            if has_right:
                cell.neighbors.append(cells[row][col + 1])

    return cells


def calc_lowest_risk(risk_map: RiskMap) -> int:
    grid = build_risk_graph(risk_map)

    start = grid[0][0]
    goal = grid[-1][-1]

    start.risk_from_start = 0

    unsettled = [start]

    while len(unsettled) > 0:
        node = heapq.heappop(unsettled)

        for neighbor in node.neighbors:
            total_risk = node.risk_from_start + neighbor.risk
            if total_risk < neighbor.risk_from_start:
                neighbor.risk_from_start = total_risk
                heapq.heappush(unsettled, neighbor)

    return goal.risk_from_start
