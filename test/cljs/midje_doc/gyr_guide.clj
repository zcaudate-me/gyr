(ns midje-doc.gyr-guide)

[[:chapter {:title "Introduction"}]]

"[gyr](https://github.com/purnam/gyr) has been spun out from the [purnam](https://github.com/purnam/purnam) core libraries into its own seperate project. By using a mixture of javascript/clojurescript syntax, `angular.js` became smaller, more readable and easier to handle. The library offers:

- [gyr.core](#gyr-core) - a simple dsl for eliminating boilerplate *angular.js*
- [gyr.test](#gyr-test) - testing macros for eliminating more boilerplate test code for services, controllers, directives and filters
- [gyr.filters](#gyr-filters) - currently undocumented

More complete examples can be seen [here](https://www.github.com/purnam/example.gyr)
"

[[:chapter {:title "Installation"}]]

"To install `gyr`, add to `project.clj` dependencies:

  `[im.chit/gyr` \"`{{PROJECT.version}}`\"`]` 

[purnam](https://github.com/purnam/purnam) come included with `gyr` so that all the language extension macros are avaliable as well.
"

[[:chapter {:title "Usage"}]]

[[:file {:src "test/cljs/midje_doc/gyr_quickstart.cljs"}]]  

[[:file {:src "test/cljs/midje_doc/api/gyr.cljs"}]]  

[[:file {:src "test/cljs/midje_doc/api/test_gyr.cljs"}]]