package net.thirdy.blackmarket.fxcontrols.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest;

import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.util.Callback;

/**
 * 
 * @author thirdy
 *
 * @param <T>
 */
public class BlackmarketSuggestionProvider<T> implements Callback<ISuggestionRequest, Collection<T>>{
	

    private final List<T> possibleSuggestions = new ArrayList<>();
    private final Object possibleSuggestionsLock = new Object();


    /**
     * Add the given new possible suggestions to this  SuggestionProvider
     * @param newPossible
     */
    public void addPossibleSuggestions(@SuppressWarnings("unchecked") T... newPossible){     
        addPossibleSuggestions(Arrays.asList(newPossible));
    }

    /**
     * Add the given new possible suggestions to this  SuggestionProvider
     * @param newPossible
     */
    public void addPossibleSuggestions(Collection<T> newPossible){
        synchronized (possibleSuggestionsLock) {
            possibleSuggestions.addAll(newPossible);
        }
    }

    /**
     * Remove all current possible suggestions
     */
    public void clearSuggestions(){
        synchronized (possibleSuggestionsLock) {
            possibleSuggestions.clear();
        }
    }

    @Override
    public final Collection<T> call(final ISuggestionRequest request) {
        List<T> suggestions = new ArrayList<>();
        if(!request.getUserText().isEmpty()){
            synchronized (possibleSuggestionsLock) {
                for (T possibleSuggestion : possibleSuggestions) {
                    if(isMatch(possibleSuggestion, request)){
                        suggestions.add(possibleSuggestion);
                    }
                }
            }
        } else {
        	synchronized (possibleSuggestionsLock) {
        		suggestions.addAll(possibleSuggestions);
        	}
        }
//        Collections.sort(suggestions, getComparator());
        return suggestions;
    }
    
    /**
     * Create a default suggestion provider based on the toString() method of the generic objects
     * @param possibleSuggestions All possible suggestions
     * @return
     */
    public static <T> BlackmarketSuggestionProvider<T> create(Collection<T> possibleSuggestions){
        return create(null, possibleSuggestions);
    }

	
	    /**
	     * Create a default suggestion provider based on the toString() method of the generic objects
	     * using the provided stringConverter
	     * 
	     * @param stringConverter A stringConverter which converts generic T into a string
	     * @param possibleSuggestions All possible suggestions
	     * @return
	     */
	    public static <T> BlackmarketSuggestionProvider<T> create(Callback<T, String> stringConverter, Collection<T> possibleSuggestions){
	        BlackmarketSuggestionProvider<T> suggestionProvider = new BlackmarketSuggestionProvider<>(stringConverter);
	        suggestionProvider.addPossibleSuggestions(possibleSuggestions);
	        return suggestionProvider;
	    }
	    
        private Callback<T, String> stringConverter;

        private final Comparator<T> stringComparator = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                String o1str = stringConverter.call(o1);
                String o2str = stringConverter.call(o2);
                return o1str.compareTo(o2str);
            }
        };

        /**
         * Create a new SuggestionProviderString
         * @param stringConverter
         */
        public BlackmarketSuggestionProvider(Callback<T, String> stringConverter){
            this.stringConverter = stringConverter;

            // In case no stringConverter was provided, use the default strategy
            if(this.stringConverter == null){
                this.stringConverter = new Callback<T, String>() {
                    @Override
                    public String call(T obj) {
                        return obj != null ? obj.toString() : ""; //$NON-NLS-1$
                    }
                };
            }
        }

        /**{@inheritDoc}*/
        protected Comparator<T> getComparator() {
            return stringComparator;
        }

        /**{@inheritDoc}*/
        protected boolean isMatch(T suggestion, ISuggestionRequest request) {
            String userTextLower = request.getUserText().toLowerCase();
            String suggestionStr = suggestion.toString().toLowerCase();
            return suggestionStr.contains(userTextLower) 
                    && !suggestionStr.equals(userTextLower);
        }
    }