SUMMARY = "Andes Technology AndeSight 5.3.0 Linux v6.1"
inherit kernel
require recipes-kernel/linux/linux-yocto.inc
FILESEXTRAPATHS =. "${FILE_DIRNAME}/files:"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
KERNEL_VERSION_SANITY_SKIP = "1"

SRCREV = "db97808ee76135daff648df6aef40a5828d2d869"
FORK = "andestech"
BRANCH = "ast-v5_3_0-branch"

SRC_URI:riscv64 = " \
    git://github.com/${FORK}/linux.git;protocol=https;branch=${BRANCH} \
    file://0001-riscv-dts-Add-support-for-dual-core-AX45MPV.patch \
    file://0002-riscv-dts-Add-support-for-single-core-AX65.patch \
    file://ae350_rv64_smp_defconfig \
    file://tweak.cfg \
"

SRC_URI:riscv32 = " \
    git://github.com/${FORK}/linux.git;protocol=https;branch=${BRANCH} \
    file://ae350_rv32_smp_defconfig \
    file://tweak.cfg \
"

LINUX_VERSION ?= "v6.1.47"
LINUX_VERSION_EXTENSION:append = "-ae350"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE = "(ae350-*)"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"
KCONF_AUDIT_LEVEL = "2"
