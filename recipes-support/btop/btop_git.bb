# Copyright (C) 2022 jaskij <jaskij@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Resource monitor that shows usage and stats for processor, memory, disks, network and processes."
HOMEPAGE = "https://github.com/aristocratos/btop"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"
SECTION = "console/utils"
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV = "a2325371d432f62b362fe1a9531a79c79fc56baa"
SRC_URI = "git://github.com/aristocratos/btop.git;protocol=https;branch=main \
           file://0001-UTF-8-locale-is-not-supported.patch \
           "

# This is how the project is set up to take the flags
# OPTFLAGS='' is cleared, because
#   1)  we supply our own optimization flags
#   2)  btop by default sets -flto, and I'm not sure
#       about the status of reproducible builds with it enabled
EXTRA_OEMAKE:append = "\
    CXX='${CXX}' \
    ADDFLAGS='${CXXFLAGS} ${LDFLAGS}' \
    QUIET=true DESTDIR='${D}' \
    PREFIX='/usr' \
    OPTFLAGS='' \
    STATIC=true \
    "

S = "${WORKDIR}/git"

do_compile() {
    oe_runmake -j ${@oe.utils.cpu_count()}
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/bin/btop ${D}${bindir}
}
