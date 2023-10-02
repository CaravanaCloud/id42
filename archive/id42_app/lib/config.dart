const _env = {
  'API_URL': String.fromEnvironment('API_URL'),
  'GOOGLE_CLIENT_ID': String.fromEnvironment('GOOGLE_CLIENT_ID'),
};

const _defaults = {
  'API_URL': 'http://localhost:8182',
  'GOOGLE_CLIENT_ID': '183177550623-csndf21r6gf7ttbfjpmv4o47dnvn819g.apps.googleusercontent.com',
};

String? config(String key){
  var value = _env[key];
  if (value == null || value.isEmpty) {
    value = _defaults[key];
  }
  return value;
}

String apiUrl(){
  return config('API_URL')!;
}

String apiPath(String path) {
  return  apiUrl()! + path;
}

String googleClientId(){
  return config('GOOGLE_CLIENT_ID')!;
}