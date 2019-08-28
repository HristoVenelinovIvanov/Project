package com.example.demo.controller

def checkOutFrom(repo) {
    git url: "https://github.com/${repo}"
}

return this