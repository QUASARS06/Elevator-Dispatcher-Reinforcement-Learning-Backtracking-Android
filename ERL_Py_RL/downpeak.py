import numpy as np
import random
import matplotlib.pyplot as plt

NUM_FLOORS = 5  # Number of Floors
HEIGHT_FLOOR = 6  # 6m
VELC_ELE = 3  # 3m/s
ELE_CAPACITY = 4
STOP_TIME = 2  # 2sec

NUM_TRIALS = 500
TRIAL_LENGTH = 1000  # 500secs
epsilon = 0.8
epsilon_decay = 0.89
temp = 11.8
temp_decay = 0.998

start_q_table = None  # None or Filename
q_table = {}

LEARNING_RATE = 0.38
DISCOUNT = 0.99

events = [0, 1, 2, 3, 4]  # e
passenger_arrival_distribution = [0.6875, 0.0625, 0.09375, 0.09375, 0.0625]

# Event e=0 means no passenger
# Event e>0 means a passenger arrives at Floor e
# {0.6875, 0.0625, 0.09375, 0.09375, 0.0625}
#  22/32,  2/32,    3/32,    3/32,    2/32

call_request = [0] * (NUM_FLOORS - 1)  # Ground floor never has a call request
# print(call_request)


class Passenger:
    def __init__(self, pname, src, dest, picked_at):
        self.pname = pname
        self.src = src
        self.dest = dest
        self.picked_at = picked_at
        self.dropped_at = 0


class Elevator:
    def __init__(self, floor):
        # self.floor = np.random.randint(0, SIZE)
        self.floor = floor
        self.velocity = 0
        self.occupancy = 0

    def action(self, choice):
        """
        Gives us 3 total movement options. (-1,0,1)
        -1 = Down
        0 = Stop
        1 = Up
        """
        self.floor += choice
        self.velocity = 3 * choice
        if self.floor > (NUM_FLOORS - 1):
            self.floor = (NUM_FLOORS - 1)
        elif self.floor < 0:
            self.floor = 0


for ci in range(16):
    c = list(str(np.binary_repr(ci, 4)))
    for p in range(5):
        for v in range(-1, 2):
            for o in range(5):
                q_table[(int(c[0]), int(c[1]), int(c[2]), int(c[3]), int(p), int(v * 3), int(o))] = [
                    np.random.uniform(-5, 0)
                    for i in range(3)]


def print_q_table():
    for key, value in q_table.items():
        print(key, value)


def get_reward(occupancy):
    rwd = (sum(call_request) * -1) - occupancy
    return rwd


def update_call_request(h, event, elev):
    # Assume that e is between 1 to 4
    if h == 0 and event != 0:
        call_request[event - 1] = 1
    elif h == 10:
        if elev.velocity == 0 and elev.occupancy < 4:
            call_request[ele.floor - 1] = 0

    if ele.floor > 0 and ele.velocity == 0 and call_request[ele.floor - 1] == 1 and ele.occupancy < 4:
        ele.occupancy += 1
    elif ele.floor == 0 and ele.velocity == 0:
        ele.occupancy = 0


final_avg = 0
avg_wt_times = []
for episode in range(NUM_TRIALS):
    ele = Elevator(1)
    call_request = [0] * (NUM_FLOORS - 1)
    avg_time = 0
    num_of_passengers = 0
    time = [0, 0, 0, 0]
    for i in range(TRIAL_LENGTH):
        # At h=0
        e = random.choices(events, weights=passenger_arrival_distribution)[0]
        if e > 0 and call_request[e - 1] == 0:
            num_of_passengers += 1
            time[e - 1] = i
        update_call_request(h=0, event=e, elev=ele)

        obs = (call_request[0], call_request[1], call_request[2], call_request[3], int(ele.floor), int(ele.velocity),
               ele.occupancy)
        # print(obs)
        if np.random.random() > epsilon:
            # GET THE ACTION
            if 0 < ele.floor < NUM_FLOORS - 1:
                action = np.argmax(q_table[obs][:])
            elif ele.floor == 0:
                action = np.argmax(q_table[obs][1:])
            else:
                action = np.argmax(q_table[obs][0:2])
        else:
            if 0 < ele.floor < NUM_FLOORS - 1:
                action = np.random.randint(0, 3)
            elif ele.floor == 0:
                action = np.random.randint(1, 3)
            else:
                action = np.random.randint(0, 2)

        # Take the action!
        ele.action(action - 1)
        b4_occ = ele.occupancy
        update_call_request(h=10, event=-1, elev=ele)
        af_occ = ele.occupancy

        if af_occ - b4_occ == 1:
            time[ele.floor - 1] = i - time[ele.floor - 1]

        reward = get_reward(ele.occupancy)

        # first we need to obs immediately after the move.
        new_obs = (
            call_request[0], call_request[1], call_request[2], call_request[3], int(ele.floor), int(ele.velocity),
            ele.occupancy)

        max_future_q = np.max(q_table[new_obs])
        current_q = q_table[obs][action]

        if reward == 0:
            avg_time += sum(time)
            time = [0, 0, 0, 0]
            new_q = 0
        else:
            new_q = (1 - LEARNING_RATE) * current_q + LEARNING_RATE * (reward + DISCOUNT * max_future_q)
        q_table[obs][action] = new_q
    # print(episode, num_of_passengers, avg_time, avg_time / num_of_passengers)
    # print('Episode:', episode, 'Passengers Spawned: ', num_of_passengers)
    yyy = avg_time / num_of_passengers
    if yyy > 0:
        avg_wt_times.append(yyy)
    final_avg += avg_time / num_of_passengers
    # print(episode, ith)
    epsilon *= epsilon_decay

print(final_avg / NUM_TRIALS)

plt.plot([i for i in range(len(avg_wt_times))], [i for i in avg_wt_times])
plt.ylabel("WT")
plt.xlabel("episode #")
plt.show()

# print_q_table()
# print('DO')
# reward = -1
# while reward != 0:
#     ele = Elevator(1)
#     call_request[0] = 1
#     call_request[3] = 1
#
#     obs = (call_request[0], call_request[1], call_request[2], call_request[3], int(ele.floor), int(ele.velocity),
#                    ele.occupancy)
#
#     if 0 < ele.floor < NUM_FLOORS - 1:
#         action = np.argmax(q_table[obs][:])
#     elif ele.floor == 0:
#         action = np.argmax(q_table[obs][1:])
#     else:
#         action = np.argmax(q_table[obs][0:2])
#
#     print(action-1)
#     ele.action(action - 1)
#     update_call_request(h=10, event=-1, elev=ele)
#
#     reward = get_reward(ele.occupancy)

# print_q_table()
