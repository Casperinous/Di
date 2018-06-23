package gr.war.Models;

import java.util.List;
import java.util.Map;

/**
 * Borrowred.
 * Original author: siggouroglou@gmail.com
 */
public class AgentPlan {

    private final String title;
    private final List<StadiumMaterials> targets;
    private final Map<String, Integer> targetsPortion;
    private boolean IsFinished = false;
    private final StadiumMaterials target;
    private final CollectionStrategy collectionStrategy;

    public AgentPlan(String title, List<StadiumMaterials> targets, Map<String, Integer> targetsPortion, StadiumMaterials target, CollectionStrategy collectionStrategy) {
        this.title = title;
        this.targets = targets;
        this.targetsPortion = targetsPortion;
        this.target = target;
        this.collectionStrategy = collectionStrategy;
    }

    public boolean IsAvailableForCarrying(StadiumMaterials stadiumMaterials){

        return this.targetsPortion.get(stadiumMaterials.getMaterialVocabulary()) > 0;
    }

    public boolean ExistInTargets(StadiumMaterials stadiumMaterials) {
        boolean isExisting = false;

        if( collectionStrategy.equals(CollectionStrategy.SPECIFIC) && target != null && stadiumMaterials.equals(target)){

                isExisting = true;
        }

        if( collectionStrategy.equals(CollectionStrategy.ALL) && targets.size() > 0){

            for(StadiumMaterials materials : targets ) {
                if( materials.equals(stadiumMaterials) ){
                    isExisting = true;
                }
            }
        }
        return isExisting;
    }

    public boolean ShouldGoHome(){
        boolean result = true;

        if( collectionStrategy.equals(CollectionStrategy.ALL) ){
            for (int value : this.targetsPortion.values()) {
                if( value > 0){
                    result = false;
                    break;
                }
            }
        }

        return result;

    }

    public String getTitle() {
        return title;
    }

    public List<StadiumMaterials> getTargets() {
        return targets;
    }

    public Map<String, Integer> getTargetsPortion() {
        return targetsPortion;
    }

    public boolean isFinished() {
        return IsFinished;
    }

    public void setFinished(boolean finished) {
        IsFinished = finished;
    }

    public StadiumMaterials getTarget() {
        return target;
    }

    public CollectionStrategy getCollectionStrategy() {
        return collectionStrategy;
    }
}
