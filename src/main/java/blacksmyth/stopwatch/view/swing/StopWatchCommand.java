/**
 * Copyright (c) 2016, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 */

package blacksmyth.stopwatch.view.swing;

/**
 * An interface that all stop watch commands are expected to implement. Based on {@link Runnable},
 * the implemented command is expected to contain all refernces to other objects its needs to successfully
 * trigger one or more {@link StopWatchViewEvent}s. 
 */
interface StopWatchCommand extends Runnable {}
