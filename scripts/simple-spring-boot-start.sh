set -euo pipefail


exec java -cp app:app/lib/* $JAVA_OPTS $1 "$@"

# If neither of those worked, then they have specified the binary they want, so
# just do exactly as they say.
exec "$@"
