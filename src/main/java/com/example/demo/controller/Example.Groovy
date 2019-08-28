package org.foo

def checkOutFrom(repo) {
    git url: "https://github.com/${repo}"
}

return this