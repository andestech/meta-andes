require conf/machine/include/riscv/tune-riscv.inc

TUNEVALID[andes-ax45mp-series] = "AndesCore AX45MP Series"

AVAILTUNES += " andes-ax45mp-series"

# AndesCore AX45MP Series
TUNE_FEATURES:tune-andes-ax45mp-series = "${TUNE_FEATURES:tune-riscv64} andes-ax45mp-series"
TUNE_ARCH:tune-andes-ax45mp-series = "riscv64"
TUNE_PKGARCH:tune-andes-ax45mp-series = "riscv64"
PACKAGE_EXTRA_ARCHS:tune-andes-ax45mp-series = "riscv64"

TUNE_CCARGS:append = "${@bb.utils.contains('TUNE_FEATURES', 'andes-ax45mp-series', ' -mtune=andes-ax45mp-series', ' ', d)}"
