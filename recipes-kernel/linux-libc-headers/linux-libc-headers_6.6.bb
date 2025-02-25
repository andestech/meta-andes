require linux-libc-headers.inc

FILESEXTRAPATHS =. "${FILE_DIRNAME}/files:"

SRC_URI:append:libc-musl = "\
    file://0001-Make-dynamic-linker-a-relative-symlink-to-libc.patch \
    file://0002-ldso-Use-syslibdir-and-libdir-as-default-pathes-to-l.patch \
    file://0003-elf.h-add-typedefs-for-Elf64_Relr-and-Elf32_Relr.patch \
   "

SRC_URI += "\
    file://0001-kbuild-install_headers.sh-Strip-_UAPI-from-if-define.patch \
"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRC_URI[sha256sum] = "d926a06c63dd8ac7df3f86ee1ffc2ce2a3b81a2d168484e76b5b389aba8e56d0"


