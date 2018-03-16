#!/bin/bash

dbus-send --session --type=method_call --dest="org.gnome.SettingsDaemon.Power"   /org/gnome/SettingsDaemon/Power   org.freedesktop.DBus.Properties.Set   string:"org.gnome.SettingsDaemon.Power.Screen"   string:"Brightness"   variant:int32:$1
