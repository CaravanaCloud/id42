#!/bin/bash
set -ex

flutter clean
flutter build web --dart-define=API_URL=""
