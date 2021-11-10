setenv bootargs earlycon=sbi root=/dev/mmcblk0p2 rootwait
load mmc 0:1 0x600000 Image
booti 0x600000 - $fdtcontroladdr
