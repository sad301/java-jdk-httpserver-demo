#!/bin/bash

if [ -e absensi.db ]; then
	rm absensi.db
fi

sqlite3 absensi.db ".read absensi.sql"
