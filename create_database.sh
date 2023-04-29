#!/bin/bash

DB_FILE=database/absensi.db
SQL_DIR=database/sql
CSV_DIR=database/csv
TEMP_IFS="$IFS"

if [ -e $DB_FILE ]; then
	rm $DB_FILE
fi

for SQL_FILE in `ls $SQL_DIR`; do
	sqlite3 $DB_FILE ".read ${SQL_DIR}/${SQL_FILE}"
done

for CSV_FILE in `ls $CSV_DIR`; do
	IFS="." read -r -a ext <<< "${CSV_FILE}"
	sqlite3 $DB_FILE ".mode csv" ".import ${CSV_DIR}/${CSV_FILE} ${ext[0]}"
done

IFS="${TEMP_IFS}"

# test
# sqlite3 $DB_FILE ".mode table" "select * from view_jenis_nomor_json"