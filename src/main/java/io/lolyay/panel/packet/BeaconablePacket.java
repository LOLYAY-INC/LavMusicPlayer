package io.lolyay.panel.packet;

/**
 * An abstract base class for packets that can be sent or received through the beacon system.
 * Extends {@link AbstractPacket} to provide common packet handling functionality.
 *
 * <p>Implementations of this class should define the specific behavior for handling
 * incoming packets in the {@link #receivePacket()} method.</p>
 */
public abstract class BeaconablePacket extends AbstractPacket {
    /**
     * Called when this packet is received by the beacon system.
     *
     * <p>Subclasses must implement this method to define the specific behavior
     * that should occur when this packet is received. This may include updating
     * application state, sending responses, or triggering other actions.</p>
     */
    public abstract void receivePacket();
}