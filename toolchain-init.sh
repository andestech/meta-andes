#!/bin/bash
set -e

EXTERNAL_TOOLCHAIN="nds64le-linux-glibc-v5d"
SYSROOT="$EXTERNAL_TOOLCHAIN/sysroot"
MULTILIB="lib64/lp64d"
USRMERGE="n"
for arg in "$@"; do
    case $arg in
        usrmerge=y) USRMERGE="y" ;;
        usrmerge=n) USRMERGE="n" ;;
    esac
done

cd "$SYSROOT"

# The external toolchain does not support multi-lib
cp -rfd "$MULTILIB"/* lib/
cp -rfd usr/"$MULTILIB"/* usr/lib/

# Correct the libc.so linker script
if [ "$USRMERGE" = "y" ]; then
    sed -i "s|/$MULTILIB/libc.so.6|/usr/lib/libc.so.6|" usr/lib/libc.so
    sed -i "s|/lib/ld-linux-riscv64-lp64d.so.1|/usr/lib/ld-linux-riscv64-lp64d.so.1|" usr/lib/libc.so
else
    sed -i "s|/$MULTILIB/libc.so.6|/lib/libc.so.6|" usr/lib/libc.so
fi

sed -i "s|/usr/lib/../$MULTILIB/libc_nonshared.a|/usr/lib/libc_nonshared.a|" usr/lib/libc.so

cd usr/lib/
for link in *.so; do
    # Skip if it's not a symbolic link (libc.so is a linker script)
    if [ ! -L "$link" ]; then
        continue
    fi

    # Find the corresponding .so.* file in the lib directory
    target_file=$(ls ../../lib/$(basename "$link").* 2>/dev/null | head -n 1)

    # Correct the symbolic link path (../../../libanl.so.1 -> ../../libanl.so.1)
    if [ -n "$target_file" ]; then
        if [ "$USRMERGE" = "y" ]; then
            ln -sf $(basename "$target_file") "$link"
        else
            ln -sf "$target_file" "$link"
        fi
    else
        echo "Warning: No matching .so.* file found for $link in $SYSROOT/lib"
    fi
done
