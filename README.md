# Heroes
Proof of concept that access the [Marvel API](https://developer.marvel.com). This make a
simple application that uses the most common Android libraries with Kotlin.
The Application should present a list of Marvel Super Heroes so the user can picks one that he likes,
The user can make search about the favourite hero, and favourite a preferable hero.




### Tech Stack

* MVVM: Model View View Model as architecture
* Kotlin: As main project language
* Kotlin Android Extensions: to access layout fields
* Dagger2: Dependency injection
* Retrofit: API calls
* Picasso: Image loading
* OkHttp3: Network client and Caching
* Gson: JSON parser
* RxKotlin: ReactiveX programming

### Workflow

#### Commit messages
All the commit messages should use the imperative mood in description, first you should use a single line commit
if necessary you can explain the commit's motivation always using the `what` and `why`.

##### Message structure
The commit message consists of the three distinct parts separated by a blank line: the title,
an optional body and the author's signature. It looks like below:
```
    [TagName]: Short description

    Body of explanation, explaning the motivation use the rule `what` and `why`, not the how.

    <Author-Signature>
```

The TagName should follow the pattern listed below:

```
[Chore]: Used to designate update a build task, configs, no production code change
[Docs]: Change documentation
[Feat]: Used to designate a new Feature
[Fix]: A bug fix
[Refactor]: User to designate a production code refactoring
[Style]: Formatting , missing semi colons, etc, no code change
[Tests]: Add tests, refactoring tests, no production code changes
``` 

### Build and Running

We use Android Studio as development tool, so to build and run this project properly we
recommend download the latest Android Studio version from [this link](https://developer.android.com/studio/).
After it clone and open on IDE, to run please refer to [this guide](https://developer.android.com/studio/run/).

After all, Enjoy!
