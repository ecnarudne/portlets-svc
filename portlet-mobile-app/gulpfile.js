var gulp = require('gulp');
var gutil = require('gulp-util');
var bower = require('bower');
var concat = require('gulp-concat');
var sass = require('gulp-sass');
var minifyCss = require('gulp-minify-css');
var rename = require('gulp-rename');
var sh = require('shelljs');

// Added variables which are needed
var $  = require('gulp-load-plugins')();
var connect    = require('gulp-connect');
var livereload = require('gulp-livereload');
var path       = require('path');

gulp.task('build', ['jade', 'less', 'html', 'ionic', 'css', 'js', 'coffee', 'images']);

var paths = {
  sass: ['./scss/**/*.scss']
};

gulp.task('default', ['sass']);

gulp.task('html', function() {
    gulp.src('app/views/**/*.html')
    .pipe(gulp.dest('www/views'))
    .pipe(livereload())
});
/* Tasks add start*/
//To convert jade files to html
gulp.task('jade', function() {
    gulp.src('app/**/*.jade')
    .pipe($.jade({ pretty: true }))
    .pipe(gulp.dest('www/'))
    .pipe(livereload())
});

// To convert less files to css  
gulp.task('less', function() {
    gulp.src('app/css/main.less')
    .pipe($.sourcemaps.init())
    .pipe($.less())
    .pipe($.sourcemaps.write('.'))
    .pipe(gulp.dest('www/css'))
    .pipe(livereload())
});

// To convert  coffee files to js 
gulp.task('coffee', function() {
    gulp.src('app/js/**/*.coffee')
    .pipe($.sourcemaps.init())
    .pipe($.coffee())
    .pipe($.sourcemaps.write('.'))
    .pipe(gulp.dest('www/js'))
    .pipe(livereload())
});

gulp.task('js', function() {
    gulp.src('app/js/**/**/*.js')
    .pipe(gulp.dest('www/js'))
    .pipe(livereload())
});

gulp.task('css', function() {
    gulp.src('app/css/**/**/*.css')
    .pipe(gulp.dest('www/css'))
    .pipe(livereload())
});

gulp.task('images', function() {
    gulp.src('app/images/**/*.*')
    .pipe(gulp.dest('www/img'))
    .pipe(livereload())
});

gulp.task('ionic', function() {
    gulp.src('app/plugins/**/**/*.*')
    .pipe(gulp.dest('www/lib'))
    .pipe(livereload())
});
/* Tasks add end*/

gulp.task('sass', function(done) {
  gulp.src('./scss/ionic.app.scss')
    .pipe(sass({
      errLogToConsole: true
    }))
    .pipe(gulp.dest('./www/css/'))
    .pipe(minifyCss({
      keepSpecialComments: 0
    }))
    .pipe(rename({ extname: '.min.css' }))
    .pipe(gulp.dest('./www/css/'))
    .on('end', done);
});

gulp.task('watch', function() {
  livereload.listen();
  gulp.watch(paths.sass, ['sass']);
  gulp.watch('app/index.html', ['html']);
  gulp.watch('app/views/**/*.html', ['html']);
  gulp.watch('app/img/**/*.*', ['copy-images']);
  gulp.watch('app/css/**/*.css', ['css']);
  gulp.watch('app/css/**/*.less', ['less']);
  gulp.watch('app/js/**/*.coffee', ['coffee']);
  gulp.watch('app/**/*.jade', ['jade']);
});

gulp.task('install', ['git-check'], function() {
  return bower.commands.install()
    .on('log', function(data) {
      gutil.log('bower', gutil.colors.cyan(data.id), data.message);
    });
});

gulp.task('git-check', function(done) {
  if (!sh.which('git')) {
    console.log(
      '  ' + gutil.colors.red('Git is not installed.'),
      '\n  Git, the version control system, is required to download Ionic.',
      '\n  Download git here:', gutil.colors.cyan('http://git-scm.com/downloads') + '.',
      '\n  Once git is installed, run \'' + gutil.colors.cyan('gulp install') + '\' again.'
    );
    process.exit(1);
  }
  done();
});

gulp.task('server', ['build', 'watch'], function(){
    $.connect.server(
        {
          root: 'www/',
          port: 3000
        }
    );
});
