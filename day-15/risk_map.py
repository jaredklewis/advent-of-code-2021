from typing import List

RiskMap = List[List[int]]


def parse_risk_map(file: str) -> List[List[int]]:
    text_file = open(file, "r")
    text = text_file.read()
    text_file.close()
    return [
        list(map(lambda char: int(char), list(line)))
        for line in text.splitlines()
    ]


def enlarge_risk_map(risk_map: RiskMap) -> RiskMap:
    mega_map = []
    risk_map_height = len(risk_map)

    increases = [
        [0, 1, 2, 3, 4],
        [1, 2, 3, 4, 5],
        [2, 3, 4, 5, 6],
        [3, 4, 5, 6, 7],
        [4, 5, 6, 7, 8]
    ]
    for meta_y in range(5):
        for meta_x in range(5):
            increase = increases[meta_y][meta_x]
            for y in range(risk_map_height):
                mega_y = y + (meta_y * risk_map_height)
                if len(mega_map) <= mega_y:
                    mega_map.append([])
                mega_map[mega_y].extend([
                  bump(risk, increase) for risk in risk_map[y]
                ])

    return mega_map


def bump(risk: int, increase: int) -> int:
    bumped = risk + increase

    if bumped <= 9:
        return bumped

    return bumped % 9
