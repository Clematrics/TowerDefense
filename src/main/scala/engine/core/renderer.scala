package engine.core

import engine.Cst

import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.Color

/**
 * Rendering is done layer by layer
 * Layer 0  - Background            : background
 * Layer 1* - BackgroundEffects     : area of effects, background effects
 * Layer 2  - GroundEntities        : ground entities
 * Layer 3* - GroundEntitiesEffects : area of effects, middle effects
 * Layer 4  - FlyingEntities        : flying entities
 * Layer 5* - FlyingEntitiesEffects : area of effects, high altitude effects
 * Layer 6* - PostProcessing        : post processing
 * Layer 7  - UserInterface         : user interface
 * Layer 8* - PrimaryUserInterface  : primary user interface
 * Layer 9* - GlobalPostProcessing  : global post processing
 * ____________________________
 * * Not currently in use
 */
object Renderer {
	private var backgroundImg            : BufferedImage = _
	private var backgroundEffectsImg     : BufferedImage = _
	private var groundEntitiesImg        : BufferedImage = _
	private var groundEntitiesEffectsImg : BufferedImage = _
	private var flyingEntitiesImg        : BufferedImage = _
	private var flyingEntitiesEffectsImg : BufferedImage = _
	private var postProcessingImg        : BufferedImage = _
	private var userInterfaceImg         : BufferedImage = _
	private var primaryUserInterfaceImg  : BufferedImage = _
	private var globalPostProcessingImg  : BufferedImage = _
	private var debugImg                 : BufferedImage = _

	private var textImg                  : BufferedImage = _

	var background            : Graphics2D   = _
	var backgroundEffects     : Graphics2D   = _
	var groundEntities        : Graphics2D   = _
	var groundEntitiesEffects : Graphics2D   = _
	var flyingEntities        : Graphics2D   = _
	var flyingEntitiesEffects : Graphics2D   = _
	var postProcessing        : Graphics2D   = _
	var userInterface         : Graphics2D   = _
	var primaryUserInterface  : Graphics2D   = _
	var globalPostProcessing  : Graphics2D   = _
	var debug                 : Graphics2D   = _

	var text                  : Graphics2D   = _

	var debugMode: Boolean = false

	resetRendering

	def resetRendering(): Unit = {
		val width  = Cst.layerResolutionWidth
		val height = Cst.layerResolutionHeight
		backgroundImg            = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		// backgroundEffectsImg     = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		groundEntitiesImg        = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		// groundEntitiesEffectsImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		flyingEntitiesImg        = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		// flyingEntitiesEffectsImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		// postProcessingImg        = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		userInterfaceImg         = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		primaryUserInterfaceImg  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		// globalPostProcessingImg  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		debugImg                 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		textImg                  = new BufferedImage(width * engine.Cst.textLayerScaling, height * engine.Cst.textLayerScaling, BufferedImage.TYPE_INT_ARGB)
	}

	def prepareRendering(): Unit = {
		background            =            backgroundImg.createGraphics
		// backgroundEffects     =     backgroundEffectsImg.createGraphics
		groundEntities        =        groundEntitiesImg.createGraphics
		// groundEntitiesEffects = groundEntitiesEffectsImg.createGraphics
		flyingEntities        =        flyingEntitiesImg.createGraphics
		// flyingEntitiesEffects = flyingEntitiesEffectsImg.createGraphics
		// postProcessing        =        postProcessingImg.createGraphics
		userInterface         =         userInterfaceImg.createGraphics
		primaryUserInterface  =  primaryUserInterfaceImg.createGraphics
		// globalPostProcessing  =  globalPostProcessingImg.createGraphics
		debug                 =                 debugImg.createGraphics

		text                  =                  textImg.createGraphics

		background            setBackground(new Color(0, 0, 0, 0))
		// backgroundEffects     setBackground(new Color(0, 0, 0, 0))
		groundEntities        setBackground(new Color(0, 0, 0, 0))
		// groundEntitiesEffects setBackground(new Color(0, 0, 0, 0))
		flyingEntities        setBackground(new Color(0, 0, 0, 0))
		// flyingEntitiesEffects setBackground(new Color(0, 0, 0, 0))
		// postProcessing        setBackground(new Color(0, 0, 0, 0))
		userInterface         setBackground(new Color(0, 0, 0, 0))
		primaryUserInterface  setBackground(new Color(0, 0, 0, 0))
		// globalPostProcessing  setBackground(new Color(0, 0, 0, 0))
		debug                 setBackground(new Color(0, 0, 0, 0))
		text                  setBackground new Color(0, 0, 0, 0)

		val width  = Cst.layerResolutionWidth
		val height = Cst.layerResolutionHeight
		background            clearRect(0, 0, width, height)
		// backgroundEffects     clearRect(0, 0, width, height)
		groundEntities        clearRect(0, 0, width, height)
		// groundEntitiesEffects clearRect(0, 0, width, height)
		flyingEntities        clearRect(0, 0, width, height)
		// flyingEntitiesEffects clearRect(0, 0, width, height)
		// postProcessing        clearRect(0, 0, width, height)
		userInterface         clearRect(0, 0, width, height)
		primaryUserInterface  clearRect(0, 0, width, height)
		// globalPostProcessing  clearRect(0, 0, width, height)
		debug                 clearRect(0, 0, width, height)
		text                  clearRect(0, 0, width * engine.Cst.textLayerScaling, height * engine.Cst.textLayerScaling)
	}

	def closeRendering(): Unit = {
		background            .dispose
		// backgroundEffects     .dispose
		groundEntities        .dispose
		// groundEntitiesEffects .dispose
		flyingEntities        .dispose
		// flyingEntitiesEffects .dispose
		// postProcessing        .dispose
		userInterface         .dispose
		primaryUserInterface  .dispose
		// globalPostProcessing  .dispose
		debug                 .dispose
		text                  .dispose
	}


	var w = 0
	var h = 0
	var offsetX = 0
	var offsetY = 0
	def computeAdjustments(): Unit = {
		val rateX = GamePanel.size.width.toDouble  / Cst.layerResolutionWidth
		val rateY = GamePanel.size.height.toDouble / Cst.layerResolutionHeight
		if (rateX > rateY){
			w = (Cst.layerResolutionWidth  * rateY).toInt
			h = (Cst.layerResolutionHeight * rateY).toInt
			offsetX = (GamePanel.size.width - w) / 2
		}
		else{
			w = (Cst.layerResolutionWidth  * rateX).toInt
			h = (Cst.layerResolutionHeight * rateX).toInt
			offsetY = (GamePanel.size.height - h) / 2
		}
	}

	def mergeLayers(g: Graphics2D) = {
		val finalImg = new BufferedImage(Cst.layerResolutionWidth, Cst.layerResolutionHeight, BufferedImage.TYPE_INT_ARGB)
		val finalG2D = finalImg.createGraphics
		finalG2D.drawImage(           backgroundImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		// finalG2D.drawImage(    backgroundEffectsImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		finalG2D.drawImage(       groundEntitiesImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		// finalG2D.drawImage(groundEntitiesEffectsImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		finalG2D.drawImage(       flyingEntitiesImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		// finalG2D.drawImage(flyingEntitiesEffectsImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		// finalG2D.drawImage(       postProcessingImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		finalG2D.drawImage(        userInterfaceImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		finalG2D.drawImage( primaryUserInterfaceImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		// finalG2D.drawImage( globalPostProcessingImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		if (debugMode)
			finalG2D.drawImage(            debugImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		finalG2D.dispose

		computeAdjustments

		g.drawImage(finalImg, offsetX, offsetY, w, h, null)
		g.drawImage(textImg, offsetX, offsetY, w, h, null)
	}
}