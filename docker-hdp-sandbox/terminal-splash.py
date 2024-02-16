#!/usr/bin/python

import curses
import subprocess
import sys

f = open('/sandbox-flavor','r');
version = f.read();
print version;
version = version.strip();
f.close();

screen = None
HINT_WIDTH = 0

class DHCPMisconfiguration(Exception):
  pass

def make_greet_window():
  H, W = screen.getmaxyx()
  greet_win = screen.subwin(H / 3 - HINT_WIDTH, W, 0, 0)
  greet_win.box()
  if version == "hdp":
   greet_win.addstr(1, 2, "Hortonworks HDP Sandbox")
  elif version == "hdf":
   greet_win.addstr(1, 2, "Hortonworks HDF Sandbox")
  greet_win.addstr(2, 2, "https://hortonworks.com/products/sandbox")
  greet_win.addstr(4, 2, "To quickly get started with the Hortonworks Sandbox, follow this tutorial:")
  if version == "hdp":
   greet_win.addstr(5, 2, "https://hortonworks.com/tutorial/hadoop-tutorial-getting-started-with-hdp/")
  elif version == "hdf":
   greet_win.addstr(5, 2, "https://hortonworks.com/tutorial/getting-started-with-hdf-sandbox")


def make_ip_window():
  H, W = screen.getmaxyx()
  ip_win = screen.subwin(H * 2 / 3, W, H / 3 - HINT_WIDTH, 0)
  ip_win.box()
  try:
    import socket
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
    s.connect(('<broadcast>',0))
    ip_hosts = s.getsockname()[0]
    s.close()
    if ip_hosts == "127.0.0.1":
      raise DHCPMisconfiguration()
  except DHCPMisconfiguration:
    ip_win.addstr(1, 2, "===================================")
    ip_win.addstr(2, 2, "Connectivity issues detected")
    ip_win.addstr(3, 2, "===================================")
    ip_win.addstr(4, 2, "Check connection of network interface.")
    ip_win.addstr(6, 2, "For details, see VM setup instructions.")
  else:
    ip_win.addstr(1, 2, "To initiate your Hortonworks Sandbox session, open a browser to this address:")
    ip_win.addstr(3, 2, "For VirtualBox:")
    ip_win.addstr(4, 6, "Welcome screen:  http://localhost:1080")
    ip_win.addstr(5, 6, "SSH:  http://localhost:4200")

    ip_win.addstr(7, 2, "For VMWare:")
    ip_win.addstr(8, 6, "Welcome screen:  http://" + ip_hosts + ":1080")
    ip_win.addstr(9, 6, "SSH:  http://" + ip_hosts + ":4200")


def init_screen():
  curses.noecho()
  make_greet_window()
  make_ip_window()


def main():
  global screen
  screen = curses.initscr()
  init_screen()

  screen.refresh()

  curses.curs_set(0)

  # Keep user on this screen
  while True:
    try:
      screen.getch()
    except KeyboardInterrupt:
      pass

  curses.endwin()


if __name__ == '__main__':
  main()

