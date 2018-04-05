#!/bin/bash

amixer get 'Master' | grep '^\s*Front Left' | sed -r 's/^[^[]*\[([0-9]+)%.*/\1/' > volume
