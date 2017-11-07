def users = [
		[firstName: "Alex", lastName: "Bolboaca"],
		[firstName: "Adi", lastName: "Bolboaca"],
		[firstName: "Alin", lastName: "Pandichi"],
		[firstName: "Andrada", lastName: "Popescu"]
]

def hasFirstName = { firstName, namedEntity ->
	namedEntity.firstName == firstName
}

println "No curry"

def hasFirstNameAdi = { namedEntity ->
	hasFirstName("Adi", namedEntity)
}
println users.findAll(hasFirstNameAdi)
println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

println "Curry"

def hasFirstNameAlex = hasFirstName.curry("Alex")
println users.findAll(hasFirstNameAlex)
println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

println "No Compose"

def hasLastName = { lastName, namedEntity ->
	namedEntity.lastName == lastName
}

def hasFirstAndLastName = { firstName, lastName, namedEntity ->
	hasFirstName(firstName, namedEntity) &&
			hasLastName(lastName, namedEntity)
}

def hasFirstNameAdiAndLastNameBolboaca = hasFirstAndLastName.curry("Adi", "Bolboaca")

println users.findAll(hasFirstNameAdiAndLastNameBolboaca)
println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

println "Naive Function Composition"

def fulfillsTwoCriteria = { firstCriteria, secondCriteria, namedEntity ->
	firstCriteria(namedEntity) &&
			secondCriteria(namedEntity)
}

def hasLastNameBolboaca = hasLastName.curry("Bolboaca")

def hasFirstNameAlexAndLastNameBolboaca = fulfillsTwoCriteria.curry(hasFirstNameAlex, hasLastNameBolboaca)

println users.findAll(hasFirstNameAlexAndLastNameBolboaca)
println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

println "Naive Function Composition II"

def fulfillsAllCriteria = { multipleCriteria, namedEntity ->
	multipleCriteria.every { criteria -> criteria(namedEntity) }
}

def hasFirstNameAlin = hasFirstName.curry("Alin")
def hasLastNamePandichi = hasLastName.curry("Pandichi")

def hasFirstNameAlinAndLastNamePandichi = fulfillsAllCriteria.curry([hasFirstNameAlin, hasLastNamePandichi])

println users.findAll(hasFirstNameAlinAndLastNamePandichi)
println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

println "Function Composition"

def nullUser = [firstName: null, lastName: null]

def predicateToComposableCriteria = { nullObject, predicate, anObject ->
	predicate(anObject) ? anObject : nullObject
}

def userPredicateToComposableCriteria = predicateToComposableCriteria.curry(nullUser)
def composableHasLastNamePandichi = userPredicateToComposableCriteria.curry(hasLastNamePandichi)
def composableHasFirstNameAlin = userPredicateToComposableCriteria.curry(hasFirstNameAlin)

// Equivalent to {params -> composableHasFirstNameAlin(composableHasLastNamePandichi(params)) }
def composedHasFirstNameAlinAndLastNamePandichi = composableHasFirstNameAlin << composableHasLastNamePandichi
def composableCriteriaToPredicate = { nullObject, criteria, objectToApplyCriteriaOn ->
	criteria(objectToApplyCriteriaOn) != nullObject
}
def userComposableCriteriaToPredicate = composableCriteriaToPredicate.curry(nullUser)

def composedPredicateHasFirstNameAlinAndLastNamePandichi =
		userComposableCriteriaToPredicate.curry(composedHasFirstNameAlinAndLastNamePandichi)

println users.findAll { composableHasFirstNameAlin(composableHasLastNamePandichi(it)) != nullUser }
println users.findAll(composedPredicateHasFirstNameAlinAndLastNamePandichi)
println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

println "Power of composability: reuse functions, change data"
def events = [
		[date: new Date(), title: "meet Joe Black"],
		[date: new Date(), title: "meeting 2"],
		[date: new Date(), title: "meeting 3"],
		[date: new Date(), title: "don't meet Joe Black"]
]
def nullEvent = [date: null, title: ""]

def hasTitleSuffix = { suffix, titledEntity ->
	titledEntity.title.endsWith(suffix)
} // works on any object with a title property
def hasTitleSuffixBlack = hasTitleSuffix.curry("Black")

def hasTitlePrefix = { prefix, titledEntity ->
	titledEntity.title.startsWith(prefix)
} // works on any object with a title property
def hasTitlePrefixMeet = hasTitlePrefix.curry("meet")

// Reuse predicatetoComposableCriteria
def eventPredicateToComposableCriteria = predicateToComposableCriteria.curry(nullEvent)
def composableHasTitleSuffixBlack = eventPredicateToComposableCriteria.curry(hasTitleSuffixBlack)
def composableHasTitlePrefixMeet = eventPredicateToComposableCriteria.curry(hasTitlePrefixMeet)

def composedHasTitlePrefixMeetAndSuffixBlack = composableHasTitlePrefixMeet << composableHasTitleSuffixBlack

// Reuse composableCriteriaToPredicate
def eventComposableCriteriaToPredicate = composableCriteriaToPredicate.curry(nullEvent)
def predicateHasTitlePrefixMeetAndSuffixBlack = eventComposableCriteriaToPredicate.curry(composedHasTitlePrefixMeetAndSuffixBlack)

println events.findAll(predicateHasTitlePrefixMeetAndSuffixBlack)
println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
