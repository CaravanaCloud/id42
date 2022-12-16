import 'package:flutter/material.dart';
import 'package:id42_app/layout.dart';
import 'dart:ui' as ui;

import 'package:id42_app/map.dart';

class HomeUI extends StatelessWidget {
  const HomeUI({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      title: 'ID42',
      home: Layout()
    );
  }
}

