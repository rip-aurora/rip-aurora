package me.memeszz.aurora.mixin.accessor;

public interface IPlayerControllerMP
{
    void setBlockHitDelay(final int p0);
    
    void setIsHittingBlock(final boolean p0);
    
    float getCurBlockDamageMP();
    
    void setCurBlockDamageMP(final float p0);
}
