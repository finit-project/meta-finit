SUMMARY = "Simple event loop for Linux"
DESCRIPTION = "libuEv is a small event loop that wraps the Linux epoll() family of APIs. \
It is similar to the more established libevent, libev and the venerable Xt(3) event loop."
SECTION = "libs"
HOMEPAGE = "https://github.com/troglobit/libuev"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ef137b7f1ae336bfa9bfccc29f7bcd41"

SRC_URI = "git://github.com/troglobit/libuev.git;protocol=https;branch=master"

SRCREV = "332f28e5e15b3d556050e774d7b3d8e35ec09006"
PV = "2.4.1+git${SRCPV}"

S = "${WORKDIR}/git"

inherit autotools gettext
