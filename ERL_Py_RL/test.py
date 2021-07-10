# import numpy as np
# print(np.random.uniform(-5, 0))
# 
# q_table = []
# for flr in range(0, 10):
#     q_table.append([])
#     for act in range(-1, 2):
#         q_table[flr].append(np.random.uniform(-5, 0))
# 
# print(q_table)
# 
# for i in range(0, 10):
#     print(i, " -> ", q_table[i], np.argmax(q_table[i][:]))

import numpy as np

# ctr = 0
# for ci in range(16):
#     c = list(str(np.binary_repr(ci, 4)))
#     for p in range(5):
#         for v in range(-1, 2):
#             for o in range(5):
#                 for action in range(-1, 2):
#                     # print(c, p, v * 3, o, action)
#                     ctr += 1
# print('CTR', ctr)
# SIZE=5
# q_table = {}
# for i in range(-SIZE+1, SIZE):
#     for ii in range(-SIZE+1, SIZE):
#         for iii in range(-SIZE+1, SIZE):
#                 for iiii in range(-SIZE+1, SIZE):
#                     q_table[((i, ii), (iii, iiii))] = [np.random.uniform(-5, 0) for i in range(4)]
#                     print(((i, ii), (iii, iiii)), q_table[((i, ii), (iii, iiii))])
# print(np.random.uniform(-5, 0))
# ctr=0
# for ci in range(16):
#     c = list(str(np.binary_repr(ci, 4)))
#     for p in range(5):
#         for v in range(-1, 2):
#             for o in range(5):
#                 ctr+=1
#                 # q_table[(int(c[0]), int(c[1]), int(c[2]), int(c[3]), int(v * 3), int(o))] = [np.random.uniform(-5, 0) for i in range(3)]
# print(ctr)
# import random
# choices = [0, 1, 2, 3, 4]
# passenger_arrival_distribution = [0.6875, 0.0625, 0.09375, 0.09375, 0.0625]
# for i in range(32):
#     print(random.choices(choices, weights=passenger_arrival_distribution)[0])
q_table = {}
for ci in range(16):
    c = list(str(np.binary_repr(ci, 4)))
    for p in range(5):
        for v in range(-1, 2):
            for o in range(5):
                q_table[(int(c[0]), int(c[1]), int(c[2]), int(c[3]), int(p), int(v * 3), int(o))] = [
                    np.random.uniform(-5, 0)
                    for i in range(3)]

print(q_table[(0, 0, 0, 0, 1, 0, 0)])

