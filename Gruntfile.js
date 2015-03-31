'use strict';
var fs = require('fs');

var parseString = require('xml2js').parseString;
// Returns the second occurence of the version number
var parseVersionFromPomXml = function() {
    var version;
    var pomXml = fs.readFileSync('pom.xml', "utf8");
    parseString(pomXml, function (err, result){
        version = result.project.version[0];
    });
    return version;
};

var useminAutoprefixer = {
    name: 'autoprefixer',
    createConfig: require('grunt-usemin/lib/config/cssmin').createConfig // Reuse cssmins createConfig
};

module.exports = function (grunt) {
    require('load-grunt-tasks')(grunt);
    require('time-grunt')(grunt);

    grunt.initConfig({
        wcc: {
            app: require('./bower.json').appPath || 'app',
            dist: 'src/main/webapp/dist'
        },
        watch: {
            bower: {
                files: ['bower.json'],
                tasks: ['wiredep']
            },
            ngconstant: {
                files: ['Gruntfile.js', 'pom.xml'],
                tasks: ['ngconstant:dev']
            },
            styles: {
                files: ['src/main/webapp/assets/styles/**/*.css']
            }
        },
        autoprefixer: {
        // not used since Uglify task does autoprefixer,
        },
        wiredep: {
            app: {
                src: ['src/main/webapp/index.html'],
                exclude: []
            },
        },
        browserSync: {
            dev: {
                bsFiles: {
                    src : [
                        'src/main/webapp/**/*.html',
                        '{.tmp/,}src/main/webapp/assets/styles/**/*.css',
                        '{.tmp/,}src/main/webapp/scripts/**/*.js'
                    ]
                }
            },
            options: {
                watchTask: true,
                proxy: "localhost:7070"
            }
        },
        clean: {
            dist: {
                files: [{
                    dot: true,
                    src: [
                        '.tmp',
                        '<%= wcc.dist %>/*',
                        '!<%= wcc.dist %>/.git*'
                    ]
                }]
            },
            server: '.tmp'
        },
        concat: {
        // not used since Uglify task does concat,
        },
        rev: {
            dist: {
                files: {
                    src: [
                        '<%= wcc.dist %>/scripts/**/*.js',
                        '<%= wcc.dist %>/assets/styles/**/*.css'
                    ]
                }
            }
        },
        useminPrepare: {
            html: 'src/main/webapp/**/*.html',
            options: {
                dest: '<%= wcc.dist %>',
                flow: {
                    html: {
                        steps: {
                            js: ['concat', 'uglifyjs'],
                            css: ['cssmin', useminAutoprefixer]
                        },
                            post: {}
                        }
                    }
            }
        },
        usemin: {
            html: ['<%= wcc.dist %>/**/*.html'],
            css: ['<%= wcc.dist %>/assets/styles/**/*.css'],
            js: ['<%= wcc.dist %>/scripts/**/*.js'],
            options: {
                assetsDirs: ['<%= wcc.dist %>', '<%= wcc.dist %>/assets/styles'],
                patterns: {
                    js: [
                        [/(assets\/images\/.*?\.(?:gif|jpeg|jpg|png|webp|svg))/gm, 'Update the JS to reference our revved images']
                    ]
                },
                dirs: ['<%= wcc.dist %>']
            }
        },
        cssmin: {
            options: {
                root: 'src/main/webapp' // Replace relative paths for static resources with absolute path
            }
        },
        ngtemplates:    {
            dist: {
                cwd: 'src/main/webapp',
                src: ['scripts/app/**/*.html', 'scripts/components/**/*.html',],
                dest: '.tmp/templates/templates.js',
                options: {
                    module: 'jhipsterApp',
                    usemin: 'scripts/app.js',
                    htmlmin:  {
                        removeCommentsFromCDATA: true,
                        collapseBooleanAttributes: true,
                        conservativeCollapse: true,
                        removeAttributeQuotes: true,
                        removeRedundantAttributes: true,
                        useShortDoctype: true,
                        removeEmptyAttributes: true
                    }
                }
            }
        },
        htmlmin: {
            dist: {
                options: {
                    removeCommentsFromCDATA: true,
                    collapseBooleanAttributes: true,
                    conservativeCollapse: true,
                    removeAttributeQuotes: true,
                    removeRedundantAttributes: true,
                    useShortDoctype: true,
                    removeEmptyAttributes: true,
                    keepClosingSlash: true
                },
                files: [{
                    expand: true,
                    cwd: '<%= wcc.dist %>',
                    src: ['*.html'],
                    dest: '<%= wcc.dist %>'
                }]
            }
        },
        // Put files not handled in other tasks here
        copy: {
            dist: {
                files: [{
                    expand: true,
                    dot: true,
                    cwd: 'src/main/webapp',
                    dest: '<%= wcc.dist %>',
                    src: [
                        '*.html',
                        'scripts/**/*.html'
                    ]
                }]
            },
            tmp:{
              expand: true,
              dot: true,
              cwd: '.tmp/cssmin',
              dest: '<%= wcc.dist %>',
              src: [
                    '*.html',
                    'assets/**/*.css'
                ]
            }
        },
        concurrent: {
            server: [
            ],
            dist: []
        },
        cdnify: {
            dist: {
                html: ['<%= wcc.dist %>/*.html']
            }
        },
        ngAnnotate: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '.tmp/concat/scripts',
                    src: '*.js',
                    dest: '.tmp/concat/scripts'
                }]
            }
        }
    });

    grunt.registerTask('server', [
        'clean:server',
        'wiredep',
        'ngconstant:dev',
        'concurrent:server',
        'browserSync',
        'watch'
    ]);

    grunt.registerTask('test', [
        'clean:server'
    ]);

    grunt.registerTask('build', [
        'clean:dist',
        'wiredep:app',
        'useminPrepare',
        'ngtemplates',
        'concurrent:dist',
        'concat',
        'copy:dist',
        'ngAnnotate',
        'cssmin',
        'autoprefixer',
        'copy:tmp',
        'uglify',
        'rev',
        'usemin',
        'htmlmin'
    ]);

    grunt.registerTask('default', [
        'test',
        'build'
    ]);
};
