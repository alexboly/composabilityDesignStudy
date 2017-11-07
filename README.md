# A design study on function composability

## What is a Design Study?

I use the term "design study" for a specific type of deliberate practice in which I take one or more design ideas and explore them as much as possible.

## What is function composability?

One of the core ideas in functional programming is to create mathematical-style functions.

This creates a few interesting design blocks:

* small functions with a general application domain
* functions that bind one argument to a value and return another function (also called currying, or partial application)
* a function resulted from the composition of two or more functions (as in j(x) = f(g(h(x))) )
* functions that transform a function into another function (meta!)

## What is this design study about?

This design study uses a simple find operation with multiple criteria to explore the world of currying and composition.

## Do you plan to work more on it?

Maybe, I don't know. I might find it interesting to revisit, or to go into more details.

## What can I do with it?

I mainly did this for myself, and decided to share it with the world as an experiment.

So read the code, and I hope you'll learn something. Even better, try to do it yourself (fork might work well for this).

Oh, and let me know if you have ideas about other design studies.
