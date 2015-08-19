# The JST Tools project

## Summary

commont/jst/core

## Install

_JST Tools_ is part of [JBoss Tools](http://jboss.org/tools) from
which it can be [downloaded and installed](http://jboss.org/tools/download)
on its own or together with the full JBoss Tools distribution.

## Get the code

The easiest way to get started with the code is to [create your own fork](http://help.github.com/forking/), 
and then clone your fork:

    $ git clone git@github.com:<you>/jbosstools-jst.git
    $ cd jbosstools-jst
    $ git remote add upstream git://github.com/jbosstools/jbosstools-jst.git
	
At any time, you can pull changes from the upstream and merge them onto your master:

    $ git checkout master               # switches to the 'master' branch
    $ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master' onto your 'master' branch
    $ git push origin                   # pushes all the updates to your fork, which should be in-sync with 'upstream'

The general idea is to keep your 'master' branch in-sync with the
'upstream/master'.

## Building JST Tools

To build _JST Tools_ requires specific versions of Java (1.7+) and
+Maven (3.1+). See this [link](https://github.com/jbosstools/jbosstools-devdoc/blob/master/building/readme.md) for more information on how to setup, run and configure build.

This command will run the build and tests:

    $ mvn clean verify

If you just want to check if things compiles/builds you can run the build without tests:

    $ mvn clean verify -DskipTest=true

JST Tools Build includes specific version of Tern IDE into JST p2 repository. By default it uses
mirrored p2 repository located at download.jboss.org. To build using latest Tern IDE use this command

    $ mvn clean verify -Dtern.repo.url=${external.url}

Where ${external.url} should be replaced with latest SNAPSHOT p2 repository URL from 
[Tern IDE Documentation](https://github.com/angelozerr/tern.java/wiki/Installation-Update-Site). For
example to test build with latest Tern IDE 1.0.0 replace ${external.url} with 
[this one](http://oss.opensagres.fr/tern.repository/1.0.0-SNAPSHOT/).

JST Tools Build is also running tests related to AngularJS features and requires access to AngularJS Eclipse
tools p2 repository during the build. By default it uses mirrored p2 repository located at download.jboss.org.
To run make build running tests using specific version of Angular JS Eclipse tools use this command

    $ mvn clean verify -Dangularjs.repo.url=${external.url}

Where ${external.url} should be replaced with latest SNAPSHOT p2 repository URL from 
[AngularJS Eclipse Documentation](https://github.com/angelozerr/angularjs-eclipse/wiki/Installation-Update-Site). For example to test build with latest AngularJS Eclipse 1.0.0 replace ${external.url} with 
[this one](http://oss.opensagres.fr/angularjs-eclipse/1.0.0-SNAPSHOT/).

Configuring Tern IDE and AngularJS Eclipse p2 repositories works only for builds from master branch. For release 
specific branches build fails because all dependencies to Tern IDE and AngularJS Eclipse are updated to only one version using version range [X.X.X.qualifier,X.X.X.qualifier] in plug-in's manifest files.

But *do not* push changes without having the new and existing unit tests pass!

## Contribute fixes and features

_JST Tools_ is open source, and we welcome anybody that wants to
participate and contribute!

If you want to fix a bug or make any changes, please log an issue in
the [JBoss Tools JIRA](https://issues.jboss.org/browse/JBIDE)
describing the bug or new feature and give it a component type of
`JST Tools provides shared plugin functionallity between JavaEE Tools and Visual Page Editor related to text editing and validation.`. Then we highly recommend making the changes on a
topic branch named with the JIRA issue number. For example, this
command creates a branch for the JBIDE-1234 issue:

	$ git checkout -b jbide-1234

After you're happy with your changes and a full build (with unit
tests) runs successfully, commit your changes on your topic branch
(with good comments). Then it's time to check for any recent changes
that were made in the official repository:

	$ git checkout master               # switches to the 'master' branch
	$ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master' onto your 'master' branch
	$ git checkout jbide-1234           # switches to your topic branch
	$ git rebase master                 # reapplies your changes on top of the latest in master
	                                      (i.e., the latest from master will be the new base for your changes)

If the pull grabbed a lot of changes, you should rerun your build with
tests enabled to make sure your changes are still good.

You can then push your topic branch and its changes into your public fork repository:

	$ git push origin jbide-1234         # pushes your topic branch into your public fork of JST Tools

And then [generate a pull-request](http://help.github.com/pull-requests/) where we can
review the proposed changes, comment on them, discuss them with you,
and if everything is good merge the changes right into the official
repository.
