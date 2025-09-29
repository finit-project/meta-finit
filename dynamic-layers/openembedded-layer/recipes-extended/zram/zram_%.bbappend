FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

inherit finit

FINIT_SYSVINIT_INHIBIT = "0"
