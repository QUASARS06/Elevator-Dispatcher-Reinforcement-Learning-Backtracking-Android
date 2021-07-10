import numpy as np

SIZE = 10  # Number of Floors

HM_EPISODES = 5000
MOVE_PENALTY = 1
PICKUP_REWARD = 8
DROP_REWARD = 10
epsilon = 0.8
EPS_DECAY = 0.9998  # Every episode will be epsilon*EPS_DECAY
SHOW_EVERY = 1000  # how often to play through env visually.

start_q_table = None  # None or Filename
q_table = []

LEARNING_RATE = 0.1
DISCOUNT = 0.95


class Passenger:
    def __init__(self, pname, src, dest):
        self.pname = pname
        self.src = src
        self.dest = dest
        self.picked = False
        self.dropped = False


class Elevator:
    def __init__(self, floor):
        # self.floor = np.random.randint(0, SIZE)
        self.floor = floor

    def action(self, choice):
        """
        Gives us 3 total movement options. (0,1,2)
        0 = Stop
        1 = Down
        2 = Up
        """
        if choice == 0:
            self.move(val=0)
        elif choice == 1:
            self.move(val=-1)
        elif choice == 2:
            self.move(val=1)

    def move(self, val=False):

        # If no value for x, move randomly
        if not val:
            self.floor += np.random.randint(-1, 2)
        else:
            self.floor += val

        # If we are out of bounds, fix!
        if self.floor < 0:
            self.floor = 0
        elif self.floor > SIZE - 1:
            self.floor = SIZE - 1


if start_q_table is None:
    for flr in range(0, 10):
        q_table.append([])
        for act in range(-1, 2):
            q_table[flr].append(np.random.uniform(-1, 0))


passengers = []


def reset():
    p1 = Passenger('P1', 4, 9)
    p2 = Passenger('P2', 6, 0)
    p3 = Passenger('P3', 5, 7)

    global passengers
    passengers = [p1, p2, p3]


def get_reward(currflr, curraction):
    rwd = -1
    if curraction == 0:
        # First we check for any pickups
        for p in passengers:
            if not p.picked and p.src == currflr:
                p.picked = True
                rwd += PICKUP_REWARD

        # Then we check for any drops
        for p in passengers:
            if p.picked and not p.dropped and p.dest == currflr:
                p.dropped = True
                rwd += DROP_REWARD
    return rwd


def is_end():
    isend = True
    for p in passengers:
        if not p.dropped:
            isend = False
            break
    return isend


episode_rewards = []
min_val = 10000
for episode in range(HM_EPISODES):
    if episode % SHOW_EVERY == 0:
        print(episode)
        print(f"on #{episode}, epsilon is {epsilon}")
        print(f"{SHOW_EVERY} ep mean: {np.mean(episode_rewards[-SHOW_EVERY:])}")
    reset()
    ele = Elevator(1)

    episode_reward = 0
    moves = []
    for i in range(200):
        obs = ele.floor
        # print(obs)
        if np.random.random() > epsilon:
            # GET THE ACTION
            action = np.argmax(q_table[obs][:])
        else:
            action = np.random.randint(0, 3)
        # action = np.argmax(q_table[obs][:])
        # Take the action!
        moves.append(action)
        ele.action(action)

        reward = get_reward(obs, action)

        # first we need to obs immediately after the move.
        new_obs = ele.floor
        if action != 0 and obs == new_obs:
            pass
            # print('Y', episode, i, action, new_obs)
        max_future_q = np.max(q_table[new_obs])
        current_q = q_table[obs][action]

        # if is_end():  # Check for end states
        #     new_q = 0
        # else:
        new_q = (1 - LEARNING_RATE) * current_q + LEARNING_RATE * (reward + DISCOUNT * max_future_q)
        q_table[obs][action] = new_q

        episode_reward += reward
        if is_end():  # break if end is reached
            # print("END")
            # print(i, " ,", episode, " -- ", is_end())
            # print(episode, i, moves)
            # print(episode, i)
            min_val = min(min_val, len(moves))
            break

    # print(episode_reward)
    episode_rewards.append(episode_reward)
    # epsilon *= EPS_DECAY

print('MIN', min_val)

for i in range(0, SIZE):
    print(i, q_table[i], np.argmax(q_table[i]))