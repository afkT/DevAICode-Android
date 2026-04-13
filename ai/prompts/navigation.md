# Navigation (Navigation Component and Screen Navigation)

This prompt defines how to add destinations, pass arguments, handle back stack, and deep links with Navigation Component. It applies to Fragment/Activity and Safe Args. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Adding or changing destinations in the NavGraph; passing arguments with Safe Args; back stack and deep links. For projects using Navigation Component + NavHostFragment.

---

## NavGraph and destinations

- **Graph** — Define in res/navigation/ (XML or Kotlin DSL); root &lt;navigation&gt; with fragment/activity destinations.
- **Destination** — Each screen is a destination with android:name (Fragment/Activity class) and android:id (for navigate by action or id).
- **Start** — Set app:startDestination for the initial screen.
- **Nested** — Use include for sub-graphs to split by feature.

---

## Navigation and arguments

- **Safe Args** — Enable Safe Args plugin in build.gradle; declare argument (name, type, nullable, defaultValue) on destination; use generated Directions and Args for type-safe navigation.
- **Pass** — NavController.navigate(Directions.xxx(args)) or navigate(id, bundle). In Fragment read from arguments or by navArgs(). If ViewModel needs args, read in init or initializeData from SavedStateHandle/arguments.
- **Avoid** — Do not pass large or complex Parcelable in Intent/Arguments; pass id or minimal keys; load detail in target from Repository by id.
- **Without Safe Args** — Use navigate(id, bundle); Fragment uses arguments?.getXxx(key); keep key and type in sync; prefer Safe Args for type safety.

---

## Back and back stack

- **Back** — NavController.navigateUp() or popBackStack(). To pass result up use SavedStateHandle or shared ViewModel (Result API, Fragment result).
- **popUpTo / inclusive** — In action set popUpTo to clear back stack to a destination; inclusive = true pops that destination too. Use for “after login clear login stack”, “exit to home”.
- **Single top / single task** — Avoid duplicate Fragment in stack; use launchSingleTop on destination or popBackStack to existing instance.

---

## ViewModel and base classes

- **NavController** — In Activity get via NavHostFragment.findNavController(fragment) or findNavController(). If the project uses AppViewModel LiveData/StateFlow for navigation (e.g. fragNavigate, fragPopBack), observe in Activity and call navController.navigate/popBackStack so that one place triggers navigation.
- **Base** — If BaseActivity holds NavHostFragment and NavController, child or Fragment requests navigation via interface/callback; avoid Fragment holding NavController (reduces coupling).

---

## Deep links

- **Declaration** — On destination add deepLink (uri or app:uri); path or pathPattern.
- **Validation** — For App Links (HTTPS) set assetlinks.json and manifest; test with adb or Intent.ACTION_VIEW.
- **Behavior** — Deep link can create new task or join existing; use NavOptions or graph for launchSingleTop, restoreState to avoid duplicate stack or lost state.

---

## Testing and notes

- **Unit** — ViewModel should not depend on NavController; assert “navigation event” LiveData/Flow; in Activity or Fragment test verify navigate is called.
- **Process death** — Navigation restores back stack; Fragment state via SavedStateHandle or onSaveInstanceState; align with ViewModel state restore.
