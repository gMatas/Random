from typing import List


class Grid:
    def __init__(self):
        self.grid_mat = Grid.__initialise_grid_mat()

    def visualise(self) -> None:
        grid_mat_element_gen = self.create_grid_mat_element_generator()

        str_rows = list()

        for ri in range(7):
            if ri % 2 == 0:
                str_rows.append('+---+---+---+')
                continue

            str_row = ''
            for ci in range(13):
                if ci % 4 == 0:
                    str_row += '|'
                elif ci % 2 != 0:
                    str_row += ' '
                elif (ci - 2) % 4 == 0:
                    str_row += next(grid_mat_element_gen)
            str_rows.append(str_row)
     
        print('\n'.join(str_rows))

    def create_grid_mat_element_generator(self):
        for ri in range(3):
            for ci in range(3):
                yield self.grid_mat[ri][ci]
            

    @staticmethod
    def __initialise_grid_mat() -> List[List[str]]:
        grid = [[' ' for _ in range(3)] for _ in range(3)]
        return grid


grid = Grid()
print('Step 1')
grid.grid_mat[1][1] = 'X'
grid.visualise()

print('Step 2')
grid.grid_mat[0][2] = 'O'
grid.visualise()
