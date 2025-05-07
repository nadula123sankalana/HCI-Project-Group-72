import React, { useRef, useState, useEffect } from 'react';
import { Canvas, useLoader, useThree } from '@react-three/fiber';
import { OrbitControls, OrthographicCamera, PerspectiveCamera } from '@react-three/drei';
import * as THREE from 'three';

function Room({ room }) {
  const wallTexture = useLoader(THREE.TextureLoader, `/textures/${room.wallTexture || 'patterned_concrete_wall_diff_1k.jpg'}`, (loader) => {
    loader.setCrossOrigin('anonymous');
  });
  const floorTexture = useLoader(THREE.TextureLoader, '/textures/wooden_floor_02_diff_1k.jpg', (loader) => {
    loader.setCrossOrigin('anonymous');
  });
  const ceilingTexture = useLoader(THREE.TextureLoader, '/textures/ceiling_tile_diff_1k.jpg', (loader) => {
    loader.setCrossOrigin('anonymous');
  });

  const width = room.width || 10;
  const height = room.height || 5;
  const depth = room.depth || 10;
  const wallColor = room.color || '#f0f0f0';

  return (
    <group>
      <mesh rotation={[-Math.PI / 2, 0, 0]} position={[0, -height / 2, 0]} receiveShadow>
        <planeGeometry args={[width, depth]} />
        <meshStandardMaterial map={floorTexture} color={wallColor} roughness={0.6} />
      </mesh>
      <mesh rotation={[Math.PI / 2, 0, 0]} position={[0, height / 2, 0]} receiveShadow>
        <planeGeometry args={[width, depth]} />
        <meshStandardMaterial map={ceilingTexture} color={wallColor} roughness={0.4} />
      </mesh>
      <mesh position={[0, 0, -depth / 2]} receiveShadow>
        <planeGeometry args={[width, height]} />
        <meshStandardMaterial map={wallTexture} color={wallColor} roughness={0.5} />
      </mesh>
      <mesh position={[0, 0, depth / 2]} rotation={[0, Math.PI, 0]} receiveShadow>
        <planeGeometry args={[width, height]} />
        <meshStandardMaterial map={wallTexture} color={wallColor} roughness={0.5} />
      </mesh>
      <mesh position={[-width / 2, 0, 0]} rotation={[0, Math.PI / 2, 0]} receiveShadow>
        <planeGeometry args={[depth, height]} />
        <meshStandardMaterial map={wallTexture} color={wallColor} roughness={0.5} />
      </mesh>
      <mesh position={[width / 2, 0, 0]} rotation={[0, -Math.PI / 2, 0]} receiveShadow>
        <planeGeometry args={[depth, height]} />
        <meshStandardMaterial map={wallTexture} color={wallColor} roughness={0.5} />
      </mesh>
    </group>
  );
}

function Chair({ item, roomHeight }) {
  const woodTexture = useLoader(THREE.TextureLoader, '/textures/wood_025_diff_1k.jpg', (loader) => {
    loader.setCrossOrigin('anonymous');
  });
  const scale = item.scale || 1;
  const color = item.color || '#8B4513';

  return (
    <group position={[item.x || 0, -roomHeight / 2, item.z || 0]} scale={scale}>
      <mesh position={[0, 0.5, 0]} castShadow receiveShadow>
        <boxGeometry args={[1, 0.1, 1]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[0, 1.2, -0.4]} castShadow receiveShadow>
        <boxGeometry args={[1, 0.8, 0.1]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[-0.4, 0.25, -0.4]} castShadow receiveShadow>
        <cylinderGeometry args={[0.05, 0.05, 0.5, 16]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[-0.4, 0.25, 0.4]} castShadow receiveShadow>
        <cylinderGeometry args={[0.05, 0.05, 0.5, 16]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[0.4, 0.25, -0.4]} castShadow receiveShadow>
        <cylinderGeometry args={[0.05, 0.05, 0.5, 16]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[0.4, 0.25, 0.4]} castShadow receiveShadow>
        <cylinderGeometry args={[0.05, 0.05, 0.5, 16]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
    </group>
  );
}

function Table({ item, roomHeight }) {
  const woodTexture = useLoader(THREE.TextureLoader, '/textures/wood_025_diff_1k.jpg', (loader) => {
    loader.setCrossOrigin('anonymous');
  });
  const scale = item.scale || 1;
  const color = item.color || '#8B4513';

  return (
    <group position={[item.x || 0, -roomHeight / 2, item.z || 0]} scale={scale}>
      <mesh position={[0, 0.8, 0]} castShadow receiveShadow>
        <boxGeometry args={[2, 0.2, 1.5]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[-0.9, 0.4, -0.65]} castShadow receiveShadow>
        <cylinderGeometry args={[0.05, 0.05, 0.8, 16]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[-0.9, 0.4, 0.65]} castShadow receiveShadow>
        <cylinderGeometry args={[0.05, 0.05, 0.8, 16]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[0.9, 0.4, -0.65]} castShadow receiveShadow>
        <cylinderGeometry args={[0.05, 0.05, 0.8, 16]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[0.9, 0.4, 0.65]} castShadow receiveShadow>
        <cylinderGeometry args={[0.05, 0.05, 0.8, 16]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
    </group>
  );
}

function Sofa({ item, roomHeight }) {
  const fabricTexture = useLoader(THREE.TextureLoader, '/textures/fabric_001_diff_1k.jpg', (loader) => {
    loader.setCrossOrigin('anonymous');
  });
  const scale = item.scale || 1;
  const color = item.color || '#4B0082';

  return (
    <group position={[item.x || 0, -roomHeight / 2, item.z || 0]} scale={scale}>
      <mesh position={[0, 0.5, 0]} castShadow receiveShadow>
        <boxGeometry args={[3, 0.5, 1]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
      <mesh position={[0, 1, -0.4]} castShadow receiveShadow>
        <boxGeometry args={[3, 1, 0.2]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
      <mesh position={[-1.3, 0.75, 0]} castShadow receiveShadow>
        <boxGeometry args={[0.4, 0.5, 1]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
      <mesh position={[1.3, 0.75, 0]} castShadow receiveShadow>
        <boxGeometry args={[0.4, 0.5, 1]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
    </group>
  );
}

function Bookshelf({ item, roomHeight }) {
  const woodTexture = useLoader(THREE.TextureLoader, '/textures/wood_025_diff_1k.jpg', (loader) => {
    loader.setCrossOrigin('anonymous');
  });
  const scale = item.scale || 1;
  const color = item.color || '#8B4513';

  return (
    <group position={[item.x || 0, -roomHeight / 2, item.z || 0]} scale={scale}>
      <mesh position={[0, 1, 0]} castShadow receiveShadow>
        <boxGeometry args={[2, 2, 0.5]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[0, 0.5, 0]} castShadow receiveShadow>
        <boxGeometry args={[2, 0.1, 0.5]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[0, 1.5, 0]} castShadow receiveShadow>
        <boxGeometry args={[2, 0.1, 0.5]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
    </group>
  );
}

function TVStand({ item, roomHeight }) {
  const woodTexture = useLoader(THREE.TextureLoader, '/textures/wood_025_diff_1k.jpg', (loader) => {
    loader.setCrossOrigin('anonymous');
  });
  const scale = item.scale || 1;
  const color = item.color || '#2F4F4F';

  return (
    <group position={[item.x || 0, -roomHeight / 2, item.z || 0]} scale={scale}>
      <mesh position={[0, 0.5, 0]} castShadow receiveShadow>
        <boxGeometry args={[3, 0.8, 0.8]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
      <mesh position={[0, 0.2, 0]} castShadow receiveShadow>
        <boxGeometry args={[3, 0.1, 0.8]} />
        <meshStandardMaterial map={woodTexture} color={color} roughness={0.7} />
      </mesh>
    </group>
  );
}

function Bed({ item, roomHeight }) {
  const fabricTexture = useLoader(THREE.TextureLoader, '/textures/fabric_001_diff_1k.jpg', (loader) => {
    loader.setCrossOrigin('anonymous');
  });
  const scale = item.scale || 1;
  const color = item.color || '#4682B4';

  return (
    <group position={[item.x || 0, -roomHeight / 2, item.z || 0]} scale={scale}>
      <mesh position={[0, 0.4, 0]} castShadow receiveShadow>
        <boxGeometry args={[2.5, 0.4, 4]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
      <mesh position={[0, 0.8, -1.8]} castShadow receiveShadow>
        <boxGeometry args={[2.5, 0.6, 0.4]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
      <mesh position={[-1.1, 0.2, -1.8]} castShadow receiveShadow>
        <cylinderGeometry args={[0.1, 0.1, 0.4, 16]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
      <mesh position={[1.1, 0.2, -1.8]} castShadow receiveShadow>
        <cylinderGeometry args={[0.1, 0.1, 0.4, 16]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
      <mesh position={[-1.1, 0.2, 1.8]} castShadow receiveShadow>
        <cylinderGeometry args={[0.1, 0.1, 0.4, 16]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
      <mesh position={[1.1, 0.2, 1.8]} castShadow receiveShadow>
        <cylinderGeometry args={[0.1, 0.1, 0.4, 16]} />
        <meshStandardMaterial map={fabricTexture} color={color} roughness={0.8} />
      </mesh>
    </group>
  );
}

function Lamp({ item, roomHeight }) {
  const metalTexture = useLoader(THREE.TextureLoader, '/textures/metal_001_diff_1k.jpg', (loader) => {
    loader.setCrossOrigin('anonymous');
  });
  const scale = item.scale || 1;
  const color = item.color || '#FFD700';

  return (
    <group position={[item.x || 0, -roomHeight / 2, item.z || 0]} scale={scale}>
      <mesh position={[0, 0.5, 0]} castShadow receiveShadow>
        <cylinderGeometry args={[0.2, 0.2, 1, 16]} />
        <meshStandardMaterial map={metalTexture} color={color} roughness={0.6} metalness={0.8} />
      </mesh>
      <mesh position={[0, 1.2, 0]} castShadow receiveShadow>
        <coneGeometry args={[0.4, 0.6, 16]} />
        <meshStandardMaterial map={metalTexture} color={color} roughness={0.6} metalness={0.8} />
      </mesh>
      <mesh position={[0, 1.5, 0]} castShadow>
        <sphereGeometry args={[0.2, 16, 16]} />
        <meshStandardMaterial color="#ffffe0" emissive="#ffffe0" emissiveIntensity={0.8} />
      </mesh>
    </group>
  );
}

function Scene({ room, furniture, viewMode, onUpdateFurniture }) {
  const { camera, gl } = useThree();
  const raycaster = useRef(new THREE.Raycaster());
  const mouse = useRef(new THREE.Vector2());
  const plane = useRef(new THREE.Plane(new THREE.Vector3(0, 1, 0), -room.height / 2));
  const [selected, setSelected] = useState(null);
  const furnitureRefs = useRef([]);
  const controlsRef = useRef();

  useEffect(() => {
    furnitureRefs.current = furniture.map(() => React.createRef());
  }, [furniture]);

  useEffect(() => {
    if (viewMode === '2D') {
      camera.position.set(0, 10, 0);
      camera.lookAt(0, 0, 0);
      if (controlsRef.current) {
        controlsRef.current.enableRotate = false;
        controlsRef.current.enableZoom = true;
        controlsRef.current.enablePan = true;
        controlsRef.current.update();
      }
    } else {
      camera.position.set(10, 10, 10);
      camera.lookAt(0, 0, 0);
      if (controlsRef.current) {
        controlsRef.current.enableRotate = true;
        controlsRef.current.enableZoom = true;
        controlsRef.current.enablePan = true;
        controlsRef.current.update();
      }
    }
  }, [viewMode, camera]);

  const handleMouseDown = (event) => {
    event.preventDefault();
    const rect = gl.domElement.getBoundingClientRect();
    mouse.current.x = ((event.clientX - rect.left) / rect.width) * 2 - 1;
    mouse.current.y = -((event.clientY - rect.top) / rect.height) * 2 + 1;

    raycaster.current.setFromCamera(mouse.current, camera);
    const intersects = raycaster.current.intersectObjects(
      furnitureRefs.current.map((ref) => ref.current).filter(Boolean),
      true
    );

    if (intersects.length > 0) {
      const index = furniture.findIndex((item) => item.id === intersects[0].object.parent.userData.id);
      if (index !== -1) {
        setSelected(index);
      }
    } else {
      setSelected(null);
    }
  };

  const handleMouseMove = (event) => {
    if (selected === null) return;

    const rect = gl.domElement.getBoundingClientRect();
    mouse.current.x = ((event.clientX - rect.left) / rect.width) * 2 - 1;
    mouse.current.y = -((event.clientY - rect.top) / rect.height) * 2 + 1;

    raycaster.current.setFromCamera(mouse.current, camera);
    const point = raycaster.current.ray.intersectPlane(plane.current, new THREE.Vector3());
    if (point) {
      onUpdateFurniture(selected, { x: point.x, z: point.z });
    }
  };

  const handleMouseUp = () => {
    setSelected(null);
  };

  useEffect(() => {
    const domElement = gl.domElement;
    domElement.addEventListener('mousedown', handleMouseDown);
    domElement.addEventListener('mousemove', handleMouseMove);
    domElement.addEventListener('mouseup', handleMouseUp);

    return () => {
      domElement.removeEventListener('mousedown', handleMouseDown);
      domElement.removeEventListener('mousemove', handleMouseMove);
      domElement.removeEventListener('mouseup', handleMouseUp);
    };
  }, [gl, camera, selected, onUpdateFurniture, room.height]);

  const zoom = Math.max(room.width, room.depth) * 1.5;

  return (
    <>
      {viewMode === '2D' ? (
        <OrthographicCamera
          makeDefault
          position={[0, 10, 0]}
          zoom={50}
          left={-zoom}
          right={zoom}
          top={zoom}
          bottom={-zoom}
          near={0.1}
          far={100}
        />
      ) : (
        <PerspectiveCamera
          makeDefault
          fov={50}
          position={[10, 10, 10]}
          near={0.1}
          far={100}
        />
      )}
      <ambientLight intensity={0.6} />
      <directionalLight
        position={[5, 10, 5]}
        intensity={1.0}
        castShadow
        shadow-mapSize-width={512}
        shadow-mapSize-height={512}
      />
      <Room room={room} />
      {furniture.map((item, index) => (
        <group key={item.id} ref={furnitureRefs.current[index]} userData={{ id: item.id }}>
          {item.type === 'chair' && <Chair item={item} roomHeight={room.height} />}
          {item.type === 'table' && <Table item={item} roomHeight={room.height} />}
          {item.type === 'sofa' && <Sofa item={item} roomHeight={room.height} />}
          {item.type === 'bookshelf' && <Bookshelf item={item} roomHeight={room.height} />}
          {item.type === 'tvstand' && <TVStand item={item} roomHeight={room.height} />}
          {item.type === 'bed' && <Bed item={item} roomHeight={room.height} />}
          {item.type === 'lamp' && <Lamp item={item} roomHeight={room.height} />}
        </group>
      ))}
      <OrbitControls
        ref={controlsRef}
        target={[0, 0, 0]}
        enableRotate={viewMode === '3D'}
        enableZoom={true}
        enablePan={true}
      />
    </>
  );
}

function DesignCanvas({ room, furniture, viewMode, onUpdateFurniture }) {
  return (
    <Canvas
      style={{ width: '100%', height: '600px' }}
      shadows
      gl={{ antialias: true }}
    >
      <Scene
        room={room}
        furniture={furniture}
        viewMode={viewMode}
        onUpdateFurniture={onUpdateFurniture}
      />
    </Canvas>
  );
}

export default DesignCanvas;