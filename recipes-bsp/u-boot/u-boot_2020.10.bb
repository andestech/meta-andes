LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"
FILESEXTRAPATHS:prepend := "${THISDIR}/files/ae350-ax45mp:"
DEPENDS:append:ae350-ax45mp = " opensbi u-boot-tools-native"

SRCREV="050acee119b3757fee3bd128f55d720fdd9bb890"
BRANCH="v2020.10"
SRC_URI:ae350-ax45mp = " \
    git://ftp.denx.de/pub/u-boot;protocol=https;branch=${BRANCH} \
    file://0001-Solve-mmc-no-partition-table-error.patch \
    file://0002-Fix-AX45MP-XIP-mode-booting-fail-in-VCU118-issue.patch \
    file://0003-Enable-printing-OpenSBI-banner.patch \
    "

SRC_URI:append:ae350-ax45mp = " \
    file://mmc-boot.txt \
"

do_compile:prepend:ae350-ax45mp() {
    export OPENSBI=${DEPLOY_DIR_IMAGE}/fw_dynamic.bin
}

do_configure:prepend:ae350-ax45mp() {
    if [ -f "${WORKDIR}/${UBOOT_ENV}.txt" ]; then
        mkimage -A riscv -O linux -T script -C none -n "U-Boot boot script" \
            -d ${WORKDIR}/${UBOOT_ENV}.txt ${WORKDIR}/boot.scr
    fi
}

do_deploy:append:ae350-ax45mp() {
    if [ -f "${WORKDIR}/boot.scr" ]; then
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 755 ${WORKDIR}/boot.scr ${DEPLOY_DIR_IMAGE}
    fi
}

do_configure[depends] += "opensbi:do_deploy"
