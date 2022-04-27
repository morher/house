var path = require('path');

module.exports = {
    mode: 'development',
    entry: './src/main/js/index.js',
    output: {
        path: __dirname,
        filename: './target/classes/public/js/bundle.js'
    },
    devtool: false,
    cache: true,
    module: {
        rules: [
            {
                test: /\.css$/,
                use: [
                  'style-loader',
                  'css-loader'
                ]
              },
            {
                test: /\.js$/,
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            }
        ]
    }
};
