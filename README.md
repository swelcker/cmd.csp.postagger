![csplogo](https://user-images.githubusercontent.com/12301571/67168219-4d618900-f3a2-11e9-9460-b79eff997c35.PNG)

# cmd.csp.postagger
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/swelcker/cmd.csp.postagger/graphs/commit-activity)
[![GitHub release](https://img.shields.io/github/release/swelcker/cmd.csp.postagger.svg)](https://GitHub.com/swelcker/cmd.csp.postagger/releases/)
[![GitHub tag](https://img.shields.io/github/tag/swelcker/cmd.csp.postagger.svg)](https://GitHub.com/swelcker/cmd.csp.postagger/tags/)
[![GitHub commits](https://img.shields.io/github/commits-since/swelcker/cmd.csp.postagger/master.svg)](https://GitHub.com/swelcker/cmd.csp.postagger/commit/)
[![GitHub contributors](https://img.shields.io/github/contributors/swelcker/cmd.csp.postagger.svg)](https://GitHub.com/swelcker/cmd.csp.postagger/graphs/contributors/)

A robust and easy-to-use toolkit for POS (Part of Speech; NLP) tagging. It's approach is to automatically construct tagging rules in the form of a binary tree.
Supports pre-trained UPOS, XPOS tagging models for about 80 languages. See folder `Models` for more details.
Used in the Cognitive Service Platform cmd.csp.


### Prerequisites

There are no prerequisites or dependencies others than java core

### Installing/Usage

To use, merge the following into your Maven POM (or the equivalent into your Gradle build script):

```xml
<repository>
  <id>github</id>
  <name>GitHub swelcker Apache Maven Packages</name>
  <url>https://maven.pkg.github.com/swelcker</url>
</repository>

<dependency>
  <groupId>cmd.csp</groupId>
  <artifactId>csppostagger</artifactId>
  <version>1.0.0</version>
</dependency>
```

Then, import cmd.csp.postagger.*;` in your application :

```java
// Example
import csppostagger.*;

	private CSPPOSTagger posTagger = new CSPPOSTagger();
	private HashMap<String, String> FREQDICT=null;

  // init tree from rules file
  posTagger.constructTreeFromLanguage(senLanguage);
  // init FREQDICT
  FREQDICT = utl.getDictionaryByLanguage(senLanguage);
...

          ... = posTagger.tagSentence(FREQDICT, 'your string or sentence");

``` or
  wordtags = CSPPOSInitialTagger.InitTagger4Sentence(FREQDICT, sen);

  int size = wordtags.size();
  wt = new String[size];
  for (int ti = 0; ti < size; ti++) {
    tokenizer.BagOfTags.put(wordtags.get(ti).word, tokenizer.BagOfTags.getInteger(wordtags.get(ti).word, 0)+1);
    CSPPOSFWObject object = Utils.getObject(wordtags, size, ti);
    CSPPOSNode firedNode = posTagger.findFiredNode(object);
    maptags.put(wordtags.get(ti).word, firedNode.conclusion);
    tokenizer.WordTagList.put(wordtags.get(ti).word, firedNode.conclusion);
    wt[ti]=wordtags.get(ti).word;
  }
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/swelcker/cmd.csp.postagger/tags). 

## Authors

* **Stefan Welcker** - *Modifications based on RDRPOSTagger* 

See also the list of [contributors](https://github.com/swelcker/cmd.csp.stemmer/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

**Find more information about RDRPOSTagger at:** [http://rdrpostagger.sourceforge.net/](http://rdrpostagger.sourceforge.net/)

The general architecture and results of the original RDRPOSTagger can be found in the following papers:

- Dat Quoc Nguyen, Dai Quoc Nguyen, Dang Duc Pham and Son Bao Pham. [RDRPOSTagger: A Ripple Down Rules-based Part-Of-Speech Tagger](http://www.aclweb.org/anthology/E14-2005). In *Proceedings of the Demonstrations at the 14th Conference of the European Chapter of the Association for Computational Linguistics*, EACL 2014, pp. 17-20, 2014. [[.PDF]](http://www.aclweb.org/anthology/E14-2005) [[.bib]](http://www.aclweb.org/anthology/E14-2005.bib)

- Dat Quoc Nguyen, Dai Quoc Nguyen, Dang Duc Pham and Son Bao Pham. [A Robust Transformation-Based Learning Approach Using Ripple Down Rules for Part-Of-Speech Tagging](http://content.iospress.com/articles/ai-communications/aic698). *AI Communications* (AICom), vol. 29, no. 3, pp. 409-422, 2016. [[.PDF]](http://arxiv.org/pdf/1412.4021.pdf) [[.bib]](http://rdrpostagger.sourceforge.net/AICom.bib)
